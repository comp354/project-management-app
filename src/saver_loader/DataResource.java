package saver_loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;

import resources.Activities;
import resources.PertData;
import resources.Projects;
import resources.TaskProgress;
import resources.Users;

/**
 * The DataResource class is used to store and manipulate the application data
 * in the current instance of execution. The class contains static variables
 * that are used to define the current instance of the data.
 * 
 * projectList contains a list of current projects loaded from the database.
 * This list can be modified by the user, and all changes are saved to the
 * database.
 * 
 * currentUser defines the user that is currently logged in. Only projects
 * associated with this user are displayed.
 * 
 * selectedProject defines the currently active Project selected by the user.
 * Modifications are made to this Project.
 * 
 * selectedActivity defines the currently active Activity selected by the user.
 * Modifications are made to this Activity
 * 
 * The class also contains methods to make saves and loads to and from the
 * database.
 * 
 * @author daveT
 *
 */

public class DataResource {

	// current active projects loaded
	public static ArrayList<Projects> projectList = new ArrayList<Projects>();

	public static ArrayList<Users> projectMembers = new ArrayList<Users>();

	// this is the currently logged in user for which the projetList will be
	// populated
	public static Users currentUser;

	// current selected project by user
	public static Projects selectedProject;

	// current selected activity by user
	public static Activities selectedActivity;

	public static String dataBase = "jdbc:sqlite:ultimate_sandwich.db";

	public static DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

	/**
	 * Method used to retrieve a project given a projectID passed in parameters.
	 * Project must be contained in the projectList.
	 * 
	 * @param projectId
	 *            the id we wish to find the project for
	 * @return the Project with the given id, if it exists. Returns null
	 *         otherwise.
	 */
	public static Projects getProjectbyProjectId(int projectId) {

		for (Projects project : projectList) {

			if (project.getId() == projectId)
				return project;
		}
		return null;
	}

	/**
	 * Method used to retrieve a project by projectName given a string passed in
	 * parameters. Project must be contained in the projectList.
	 * 
	 * @param name
	 *            the name we wish to find the project for
	 * @return the Project with the given name, if it exists. Returns null
	 *         otherwise.
	 */
	public static Projects getProjectbyProjectName(String name) {

		for (Projects project : projectList) {

			if (project.getProjectName().equals(name))
				return project;
		}
		return null;
	}

	/**
	 * This method removes the Project passed as parameters from the database.
	 * All associated entries in Activities and relationship tables are removed
	 * as well. The project is also removed from the projectList.
	 * 
	 * @param project
	 *            Project we wish to delete
	 */
	public static void removeProject(Projects project) {
		projectList.remove(project);// removes project from projectList

		// query database and remove project
		Connection connection = DataResource.createConnectionToDB(dataBase);
		String sql;
		PreparedStatement ps;

		try {

			// delete the project
			// cascade takes care of associated tuples in other tables
			sql = ("DELETE FROM projects WHERE id=" + project.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			ArrayList<Activities> actList = project.getActivityList();

			// delete the activities associated with this project
			// cascade takes care of associated tuples in other tables
			for (Activities acts : actList) {
				sql = ("DELETE FROM activities WHERE id=" + acts.getId());
				ps = connection.prepareStatement(sql);
				ps.executeUpdate();
			}

			sql = ("DELETE FROM activity_user_project_relationships WHERE project_id=" + project.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			sql = ("DELETE FROM user_project_relationships WHERE project_id=" + project.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);

	}

	/**
	 * This method deletes the Activity passed as parameters from the database
	 * All associated tuples in other tables are also removed.
	 * 
	 * @param A
	 *            Activity we wish to delete
	 */
	public static void deleteActivity(Activities A) {
		Connection connection = DataResource.createConnectionToDB(dataBase);
		String sql;
		PreparedStatement ps;

		try {

			if(!A.isPertGenerated()){
			
				sql = ("DELETE FROM Pert_data WHERE activity_id=" + A.getId());
				ps = connection.prepareStatement(sql);
				ps.executeUpdate();
				
			}
						
			// delete activity from activity_project_relationships in database
			sql = ("DELETE FROM activity_project_relationships WHERE activity_id=" + A.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			// delete activity from activity_edge_relationship in database
			sql = ("DELETE FROM activity_edge_relationship WHERE from_activity_id=" + A.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

			sql = ("DELETE FROM activity_user_project_relationships WHERE activity_id=" + A.getId());
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			
		
			

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);
	}

	/**
	 * This method is used to delete an association between 2 Activities from
	 * the database. Given 2 integers representing Activity ID's, the method
	 * queries the database and removes the corresponding tuple. The result is
	 * that the Activities will no longer have an association between them.
	 * 
	 * @param activityBefore
	 *            Activity ID for the origin Activity who's edge is to be
	 *            removed
	 * @param activityAfter
	 *            Activity ID for the destination Activity who's edge is to be
	 *            removed
	 */
	public static void deleteEdgeFromDB(int activityBefore, int activityAfter) {
		Connection connection = DataResource.createConnectionToDB(dataBase);
		String sql;
		PreparedStatement ps;

		try {

			// Delete edge in database between the activityBefore and
			// activityAfter
			sql = ("DELETE FROM activity_edge_relationship WHERE from_activity_id=" + activityBefore
					+ " AND to_activity_id=" + activityAfter);
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);
	}

	/**
	 * This method is used to delete every association between an activity and a
	 * user from the database.
	 * 
	 * @param activityId
	 *            Activity ID for the Activity who's members are to be removed
	 */
	public static void resetActivityMembers(int activityId) {
		Connection connection = DataResource.createConnectionToDB(dataBase);
		String sql;
		PreparedStatement ps;

		try {

			// Delete members in database associated with the activity
			sql = ("DELETE FROM activity_user_project_relationships WHERE activity_id=" + activityId);
			ps = connection.prepareStatement(sql);
			ps.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);
	}

	public static void load(String projectName, String date, int projectID, int managerID,
			String description, double budget, ResultSet resultn, ArrayList<Activities> activityList) {
		
			Connection connection = DataResource.createConnectionToDB(DataResource.dataBase);
		
		try {
			// getting all users associated with project
			PreparedStatement ps1 = connection
					.prepareStatement("SELECT user_id FROM user_project_relationships WHERE " + "project_id = ?");
			ps1.setInt(1, projectID);
			ResultSet result1 = ps1.executeQuery();

			ArrayList<Users> userList = new ArrayList<Users>();

			while (result1.next()) {
				// memeberIds.add(result1.getInt(1));//got all userids
				// associated with project
				int userID = result1.getInt(1);

				PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM users WHERE " + "id = ?");
				ps2.setInt(1, userID);
				ResultSet result2 = ps2.executeQuery();

				while (result2.next()) {
					String username = result2.getString(4);
					String first_name = result2.getString(2);
					String last_name = result2.getString(3);
					String password = result2.getString(5);
					int id = result2.getInt(1);
					String userType = result2.getString(6);

					userList.add(new Users(username, first_name, last_name, password, id, userType));
				}

			}

			while (resultn.next()) {
				// have all activities associated with project

				PreparedStatement ps5 = connection.prepareStatement("SELECT * FROM activities WHERE " + "id = ?");
				ps5.setInt(1, resultn.getInt(1));
				ResultSet result5 = ps5.executeQuery();

				while (result5.next()) {
					// now create activities and add to activityList
					int id = result5.getInt(1);
					String name = result5.getString(2);
					String desc = result5.getString(3);
					Date start = dateFormatter.parse(result5.getString(4));
					Date end = dateFormatter.parse(result5.getString(5));
					TaskProgress progress = TaskProgress.valueOf(result5.getString(6));
					int activityBudget = result5.getInt(7);
					Date target = dateFormatter.parse(result5.getString(8));

					if(hasPertData(id)){

						activityList.add(new Activities(desc, start, end, name, id, progress, activityBudget,false));
					}else{
						activityList.add(new Activities(desc, start, end, name, id, progress, activityBudget,true));
					}
				}

			}

			Projects project = new Projects(projectName, userList, date, projectID, managerID, description, budget);

			for (Activities acts : activityList) {
				project.addActivity(acts);// adding each activity to the
				// project

			}

			// for each activity query activity table relation to get
			// dependent activities
			for (Activities activity : activityList) {
				// make database call
				PreparedStatement psE = connection.prepareStatement(
						"SELECT to_activity_id FROM activity_edge_relationship WHERE " + "from_activity_id = ?");
				psE.setInt(1, activity.getId());
				ResultSet resultE = psE.executeQuery();
				while (resultE.next()) {
					for (Activities dependent_activity : activityList) {
						if (dependent_activity.getId() == resultE.getInt(1)) {
							project.addArrow(activity, dependent_activity);
						}
					}
				}

				PreparedStatement ps6 = connection.prepareStatement(
						"SELECT user_id from activity_user_project_relationships where activity_id = ?");
				ps6.setInt(1, activity.getId());
				ResultSet result6 = ps6.executeQuery();
				ArrayList<Users> tmp = new ArrayList<Users>();
				while (result6.next()) {
					for (Users member : projectMembers) {
						if (member.getID() == result6.getInt(1)) {
							tmp.add(member);
						}
					}
				}
				activity.setMemberList(tmp);
			}

			// creates projects with activities
			projectList.add(project);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		closeConnection(connection);
	}

	private static boolean hasPertData(int id) {

		try {
			Connection connection = DataResource.createConnectionToDB(dataBase);
			PreparedStatement ps1 = connection.prepareStatement("SELECT sd FROM Pert_data WHERE activity_id = ?");
			ps1.setInt(1, id);
			ResultSet result1 = ps1.executeQuery();

			if(result1.next()) {
				closeConnection(connection);
				return true;

			}else{

				closeConnection(connection);
				return false;

			}
		}catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
			
		return false;

	}

	public static void loadMemberDataFromDB() {
		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps;

		try {

			loadStart();

			PreparedStatement ps4 = connection.prepareStatement(
					"SELECT distinct project_id from activity_user_project_relationships where user_id = ?");
			ps4.setInt(1, currentUser.getID());
			ResultSet result4 = ps4.executeQuery();

			while (result4.next()) {
				ps = connection.prepareStatement("SELECT * FROM projects WHERE id = ?");
				ps.setInt(1, result4.getInt(1));
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					// we have project ids
					// projIds.add(result.getInt(1));
					int projectID = rs.getInt(1);
					int managerID = rs.getInt(6);
					String projectName = rs.getString(2);
					String description = rs.getString(4);
					double budget = rs.getDouble(5);
					String date = rs.getString(3);

					ArrayList<Activities> activityList = new ArrayList<Activities>();

					// query activity relation table to get activities
					// associated
					// with project
					PreparedStatement psn = connection.prepareStatement(
							"SELECT activity_id FROM activity_user_project_relationships WHERE project_id = ? and user_id = ?");
					psn.setInt(1, projectID);
					psn.setInt(2, currentUser.getID());
					ResultSet resultn = psn.executeQuery();

					load(projectName, date, projectID, managerID, description, budget, resultn,
							activityList);
				}
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);
	}

	/**
	 * Method is used to load from database. The method builds each project
	 * associated with the current User ID logged into the system. Each project
	 * is build, with it's Activities and dependencies added, and the result is
	 * populated in the projectList static variable.
	 * 
	 */
	public static void loadManagerDataFromDB() {
		// query user_project_relationship
		// get all projID where userID = currentUserID

		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps;

		try {
			loadStart();

			ps = connection.prepareStatement("SELECT * FROM projects " + "WHERE manager_id =?;");
			ps.setInt(1, currentUser.getID());
			ResultSet result = ps.executeQuery();

			while (result.next()) {
				// we have project ids
				// projIds.add(result.getInt(1));
				int projectID = result.getInt(1);
				int managerID = result.getInt(6);
				String projectName = result.getString(2);
				String description = result.getString(4);
				double budget = result.getDouble(5);
				String date = result.getString(3);

				ArrayList<Activities> activityList = new ArrayList<Activities>();

				// query activity relation table to get activities associated
				// with project
				PreparedStatement ps4 = connection.prepareStatement(
						"SELECT activity_id FROM activity_project_relationships WHERE " + "project_id = ?");
				ps4.setInt(1, projectID);
				ResultSet result4 = ps4.executeQuery();

				load(projectName, date, projectID, managerID, description, budget, result4, activityList);
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		closeConnection(connection);
	}

	/**
	 * Method loadStart is used by both loadManagerDataFromDB and loadMemberDataFromDB. This method
	 * loads the common elements of both user types.
	 * @param connection
	 */
	private static void loadStart() {
		// get project members
		
			Connection connection = DataResource.createConnectionToDB(DataResource.dataBase);
		try {
			PreparedStatement psTotMembers = connection.prepareStatement("SELECT * FROM users where user_type = 'MEMBER';");
			ResultSet resultTotMembers = psTotMembers.executeQuery();

			while (resultTotMembers.next()) {
				String username = resultTotMembers.getString(4);
				String first_name = resultTotMembers.getString(2);
				String last_name = resultTotMembers.getString(3);
				String password = resultTotMembers.getString(5);
				int id = resultTotMembers.getInt(1);
				String userType = resultTotMembers.getString(6);

				projectMembers.add(new Users(username, first_name, last_name, password, id, userType));
			}
			// set projectCount to max project id from database

			PreparedStatement ps3 = connection.prepareStatement("SELECT max(id) FROM projects;");
			ResultSet result3 = ps3.executeQuery();

			if (result3.next()) {
				Projects.setProjectCount(result3.getInt(1));
			}

			// set activityCount to max activity id from database
			ps3 = connection.prepareStatement("SELECT max(id) FROM activities;");
			result3 = ps3.executeQuery();

			if (result3.next()) {
				Activities.setActivityCount(result3.getInt(1));
			}
			
			
			
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		closeConnection(connection);
		
	}

	/**
	 * This method is used to save changes to the database. The current instance
	 * of projectList, which contains all changes the user has made, is
	 * iterated. All new values are inserted, and any changed values replace
	 * their associated tuples. Loops through projectList and inserts projects,
	 * users, activities, dependencies to database.
	 */
	public static void saveToDB() {
		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps;

		try{

			String projectName, description, date;
			int projectID, managerID;
			double budget;

			// load projects in projects table in database
			for (Projects projects : projectList) {
				projectID = projects.getId();
				description = projects.getDescription();
				date = projects.getDate();
				projectName = projects.getProjectName();
				managerID = projects.getManagerID();
				budget = projects.getBudget();

				String sql = ("INSERT OR REPLACE INTO projects(id, name, date, description, budget, manager_id) "
						+ "VALUES (?, ?, ?, ?, ?, ?)");
				ps = connection.prepareStatement(sql);
				ps.setInt(1, projectID);
				ps.setString(2, projectName);
				ps.setString(3, date);
				ps.setString(4, description);
				ps.setDouble(5, budget);
				ps.setInt(6, managerID);
				ps.executeUpdate();

				int userID;
				// for each project, insert the list of users associated with
				// that project into the database
				for (Users user : projects.getUserList()) {
					userID = user.getID();
					sql = ("INSERT OR REPLACE INTO user_project_relationships(project_id, user_id) VALUES " + "(?, ?)");
					ps = connection.prepareStatement(sql);
					ps.setInt(1, projectID);
					ps.setInt(2, userID);
					ps.executeUpdate();
				}

				// for each project, insert the list of activities associated
				// with that project into the database
				int activityID, dependentActivityID, activityBudget;
				String actLabel, actDescription, start, end, progress, targetDate;

				for (Activities activity : projects.getActivityList()) {
					activityID = activity.getId();
					actLabel = activity.getLabel();
					actDescription = activity.getDescription();
					progress = activity.getProgress().name();
					start = dateFormatter.format(activity.getStartDate());
					end =  dateFormatter.format(activity.getEndDate());
					activityBudget = activity.getBudget();
					
					sql = ("INSERT OR REPLACE INTO activities(id, label, description, startdate, endate, progress, budget, finisheddate) VALUES "
							+ "(?, ?, ?, ?, ?, ?, ?, ?)");

					ps = connection.prepareStatement(sql);
					ps.setInt(1, activityID);
					ps.setString(2, actLabel);
					ps.setString(3, actDescription);
					ps.setString(4, start);
					ps.setString(5, end);
					ps.setString(6, progress);
					ps.setInt(7, activityBudget);
				

					if(isfinished(activity)){

						ps.setString(8,dateFormatter.format(new Date()));

					}else{
						ps.setString(8, "");
					}
					ps.executeUpdate();

					sql = ("INSERT OR REPLACE INTO activity_project_relationships(project_id, activity_id) VALUES "
							+ "(?, ?)");
					ps = connection.prepareStatement(sql);
					ps.setInt(1, projectID);
					ps.setInt(2, activityID);
					ps.executeUpdate();

					int memberID;

					for (Users member : activity.getMemberList()) {
						memberID = member.getID();

						sql = ("INSERT OR REPLACE INTO activity_user_project_relationships(activity_id, user_id, project_id) VALUES "
								+ "(?, ?, ?)");
						ps = connection.prepareStatement(sql);
						ps.setInt(1, activityID);
						ps.setInt(2, memberID);
						ps.setInt(3, projectID);
						ps.executeUpdate();
					}

					Set<DefaultEdge> edges = projects.getArrowSet();
					// for currently selected activity, add all the edges to
					// activity_edge_relationship
					for (DefaultEdge e : edges) {
						if (activityID == projects.getActivityBefore(e).getId()) {
							dependentActivityID = projects.getActivityAfter(e).getId();
							// if the activityID is a before edge, put the
							// before and after edge into table under
							// from_activity_id and to_activity_id
							sql = ("INSERT OR REPLACE INTO activity_edge_relationship(from_activity_id, to_activity_id) VALUES "
									+ "(?, ?)");
							ps = connection.prepareStatement(sql);
							ps.setInt(1, activityID);
							ps.setInt(2, dependentActivityID);
							ps.executeUpdate();
						}
					}
				}
			}

		}catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		closeConnection(connection);
	}

	/**
	 * Method used to set the Database to the supplied string
	 * 
	 * @param db
	 *            String with filename of new Database
	 */
	public static void setDatabase(String db) {
		dataBase = db;
	}

	/**
	 * Method used to save an Activity to the database. This method is called after a new
	 * activity is created and after an activity has been edited in the Activity_edit form.
	 * @param selectedActivity
	 */
	public static void saveActivity(Activities selectedActivity)
	{
		//save the tuple in Activities table in the database where that id is equal to the selected activity id
		Connection connection = createConnectionToDB(dataBase);

		int activityID, dependentActivityID, activityBudget;

		String actLabel, actDescription, start, end, progress,targetDate;

		activityID = selectedActivity.getId();
		actLabel = selectedActivity.getLabel();
		actDescription = selectedActivity.getDescription();
		start = dateFormatter.format(selectedActivity.getStartDate());
		end =  dateFormatter.format(selectedActivity.getEndDate());
		progress = selectedActivity.getProgress().name();
		activityBudget = selectedActivity.getBudget();


		String sql = ("INSERT OR REPLACE INTO activities(id, label, description, startdate, endate, progress, budget, finisheddate) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?)");

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, activityID);
			ps.setString(2, actLabel);
			ps.setString(3, actDescription);
			ps.setString(4, start);
			ps.setString(5, end);
			ps.setString(6, progress);
			ps.setInt(7, activityBudget);
			
			if(isfinished(selectedActivity)){

				ps.setString(8,dateFormatter.format(new Date()));

			}else{
				ps.setString(8, " ");
			}
			ps.executeUpdate();

			sql = ("INSERT OR REPLACE INTO activity_project_relationships(project_id, activity_id) VALUES "
					+ "(?, ?)");
			ps = connection.prepareStatement(sql);
			ps.setInt(1, selectedProject.getId());
			ps.setInt(2, activityID);
			ps.executeUpdate();

			int memberID;

			for (Users member : selectedActivity.getMemberList()) {
				memberID = member.getID();

				sql = ("INSERT OR REPLACE INTO activity_user_project_relationships(activity_id, user_id, project_id) VALUES "
						+ "(?, ?, ?)");
				ps = connection.prepareStatement(sql);
				ps.setInt(1, activityID);
				ps.setInt(2, memberID);
				ps.setInt(3, selectedProject.getId());
				ps.executeUpdate();
			}

			Set<DefaultEdge> edges = selectedProject.getArrowSet();
			// for currently selected activity, add all the edges to
			// activity_edge_relationship
			for (DefaultEdge e : edges) {
				if (activityID == selectedProject.getActivityAfter(e).getId()) {
					dependentActivityID = selectedProject.getActivityBefore(e).getId();
					// if the activityID is a before edge, put the
					// before and after edge into table under
					// from_activity_id and to_activity_id
					sql = ("INSERT OR REPLACE INTO activity_edge_relationship(from_activity_id, to_activity_id) VALUES "
							+ "(?, ?)");
					ps = connection.prepareStatement(sql);
					ps.setInt(1, dependentActivityID);
					ps.setInt(2, activityID);
					ps.executeUpdate();
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection(connection);
	}

	private static boolean isfinished(Activities activity) {

		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps;
		ResultSet result;
		TaskProgress db = null;


		try {
			ps = connection.prepareStatement(
					"SELECT * from activities where id = ?");

			ps.setInt(1, activity.getId());
			result = ps.executeQuery();

			while (result.next()) {

				db  = TaskProgress.valueOf(result.getString(6));

			}

			if(db == TaskProgress.started){
				if(activity.getProgress() == TaskProgress.complete){

					closeConnection(connection);
					return true;
				}
			}
		}catch(Exception e){

			e.printStackTrace();
		}

		closeConnection(connection);
		
		return false;
	}

	/**
	 * Method saveProject saves the fields of the selected project to the database.
	 * @param selectedProject
	 */
	public static void saveProject(Projects selectedProject){

		Connection connection = DataResource.createConnectionToDB(dataBase);

		String projectName, description, date;
		int projectID, managerID;
		double budget;

		try {

			PreparedStatement ps;

			// load projects in projects table in database
			projectID = selectedProject.getId();
			description = selectedProject.getDescription();
			date = selectedProject.getDate();
			projectName = selectedProject.getProjectName();
			managerID = selectedProject.getManagerID();
			budget = selectedProject.getBudget();

			int userID = currentUser.getID();
			// for each project, insert the list of users associated with
			// that project into the database

			String sql = ("INSERT OR REPLACE INTO user_project_relationships(project_id, user_id) VALUES " + "(?, ?)");
			ps = connection.prepareStatement(sql);
			ps.setInt(1, projectID);
			ps.setInt(2, userID);
			ps.executeUpdate();


			sql = ("INSERT OR REPLACE INTO projects(id, name, date, description, budget, manager_id) "
					+ "VALUES (?, ?, ?, ?, ?, ?)");
			ps = connection.prepareStatement(sql);
			ps.setInt(1, projectID);
			ps.setString(2, projectName);
			ps.setString(3, date);
			ps.setString(4, description);
			ps.setDouble(5, budget);
			ps.setInt(6, managerID);
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closeConnection(connection);

	}
	/*****************************helper functions to connect to and close database connections***************/

	/**
	 * A helper function to connect to the database. This method returns an active connection to the database.
	 * @param database
	 * @return
	 */
	public static Connection createConnectionToDB(String database)
	{
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(dataBase);
		}catch (Exception exception) {
			System.out.println(exception.getMessage());
		}
		return connection;
	}

	/**
	 * A helper function to close the given connection to the database.
	 * @param connection
	 */
	public static void closeConnection(Connection connection)
	{
		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}
	}

	public static void addActivityOnNodeInfo(Activities activity){
		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(
					"SELECT * from Activity_Node where activity_id = ?");

			ps.setInt(1, activity.getId());
			ResultSet result4 = ps.executeQuery();

			while (result4.next()) {
				activity.setEarliestStart(result4.getInt(2));
				activity.setEarliestFinish(result4.getInt(3));
				activity.setLatestStart(result4.getInt(4));
				activity.setLatestFinish(result4.getInt(5));
			}
			
			closeConnection(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the all the data for PERT analysis for 1 activity
	 * 
	 * if the data was never specified by a user it will be generated by historical data that has the same max duration 
	 * 
	 * 
	 * @param activity
	 * @return PertData for that Activity
	 */


	public static PertData getPertData(Activities activity){

		PertData pert = null;

		Connection connection = DataResource.createConnectionToDB(dataBase);
		PreparedStatement ps, ps2 ;
		ResultSet result;
		ResultSet result2;
		try {
			ps = connection.prepareStatement(
					"SELECT * from Pert_data where activity_id = ?");

			ps.setInt(1, activity.getId());
			result = ps.executeQuery();

			while (result.next()) {
				pert = new PertData(activity.getId(), result.getInt(2), result.getInt(3), result.getInt(4), result.getDouble(5));
			}

			//if no data is specified it will generate the data(this will be dynamic and change every time as more activities get added)
			//Self Learning		
			if(pert == null){

				//get the activity's information for pestimisticTime ,optimisticTime,mostLikelyTime
				ps = connection.prepareStatement(
						"SELECT * from Activity_Node where activity_id = ?");
				ps.setInt(1, activity.getId());
				ResultSet result4 = ps.executeQuery();

				int pestimisticTime = (Integer) null;
				int optimisticTime= (Integer) null;
				int mostLikelyTime= (Integer) null;
				double standardDeviation= (Double) null;		

				while (result4.next()) {

					pestimisticTime = result.getInt(5)- result.getInt(2);
					optimisticTime = result.getInt(4) - result.getInt(3); 
					mostLikelyTime = (result.getInt(5)-result.getInt(4))/2;
				}		

				//this will calculate the Standard Deviation
				ps = connection.prepareStatement(
						"SELECT activity_id from Activity_Node where  Activity_Node.LF - Activity_Node.ES = ?");
				ps.setDouble(1, (double)pestimisticTime );
				result = ps.executeQuery();

				ArrayList<Integer> std = new ArrayList<Integer>();

				while(result.next()){
					ps2 = connection.prepareStatement(
							"SELECT finisheddate-endate from activities where  id = ?");
					ps2.setDouble(1, result.getInt(1) );
					result2 = ps.executeQuery();
					while(result2.next()){
						std.add(result.getInt(1));
					}

					//math to calculate Standard deviation ( Sqrt of ( |E(x - average)^2|/n )
					int sum =0;
					for(int i : std){	sum += i;	}
					int average = sum/std.size();
					for(int i : std){	sum = i - average;	}

					standardDeviation = Math.sqrt((sum* sum)/std.size());

					pert = new PertData(activity.getId(), pestimisticTime, optimisticTime, mostLikelyTime, standardDeviation);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection(connection);
		
		return pert;
	}

	public static void setPertData(PertData pert){

		try {
			Connection connection = DataResource.createConnectionToDB(dataBase);
			String sql = ("REPLACE INTO Pert_data(activity_id, P, O, M, Sd) VALUES "
					+ "(?, ?, ?, ?, ?)");
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, pert.getActivityId() );
			ps.setInt(2, pert.getPestimisticTime());
			ps.setInt(3, pert.getOptimisticTime());
			ps.setInt(4, pert.getMostLikelyTime());
			ps.setDouble(5, pert.getStandardDeviation());
			ps.executeUpdate();
			closeConnection(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
