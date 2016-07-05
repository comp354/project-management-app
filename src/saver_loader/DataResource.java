package saver_loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;

import resources.Activities;
import resources.Projects;
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

	/**
	 * Method used to retreive a project given a projectID passed in parameters.
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
	 * Method used to retreive a project by projectName given a string passed in
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
		Connection connection = null;
		String sql;
		PreparedStatement statement;

		try {
			connection = DriverManager.getConnection(dataBase);
			// delete the project
			// cascade takes care of associated tuples in other tables
			sql = ("DELETE FROM projects WHERE id=" + project.getId());
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

			ArrayList<Activities> actList = project.getActivityList();

			// delete the activities associated with this project
			// cascade takes care of associated tuples in other tables
			for (Activities acts : actList) {
				sql = ("DELETE FROM activities WHERE id=" + acts.getId());
				statement = connection.prepareStatement(sql);
				statement.executeUpdate();
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}

	}

	/**
	 * This method deletes the Activity passed as parameters from the database
	 * All associated tuples in other tables are also removed.
	 * 
	 * @param A
	 *            Activity we wish to delete
	 */
	public static void deleteActivity(Activities A) {
		Connection connection = null;
		String sql;
		PreparedStatement statement;

		try {
			connection = DriverManager.getConnection(dataBase);
			// delete activity from activities table in database
			sql = ("DELETE FROM activities WHERE id=" + A.getId());
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

			// delete activity from activity_project_relationships in database
			sql = ("DELETE FROM activity_project_relationships WHERE activity_id=" + A.getId());
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

			// delete activity from activity_edge_relationship in database
			sql = ("DELETE FROM activity_edge_relationship WHERE from_activity_id=" + A.getId());
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}
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
		Connection connection = null;
		String sql;
		PreparedStatement statement;

		try {
			connection = DriverManager.getConnection(dataBase);

			// Delete edge in database between the activityBefore and
			// activityAfter
			sql = ("DELETE FROM activity_edge_relationship WHERE from_activity_id=" + activityBefore
					+ " AND to_activity_id=" + activityAfter);
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}
	}
	
	/**
	 * This method is used to delete every association between an activity and
	 * a user from the database.
	 * 
	 * @param activityId
	 *            Activity ID for the Activity who's members are to be
	 *            removed
	 */
	public static void resetActivityMembers(int activityId) {
		Connection connection = null;
		String sql;
		PreparedStatement statement;
		
		try {
			connection = DriverManager.getConnection(dataBase);

			// Delete members in database associated with the activity
			sql = ("DELETE FROM activity_user_project_relationships WHERE activity_id=" + activityId);
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}
	}

	/**
	 * Method is used to load from database. The method builds each project
	 * associated with the current User ID logged into the system. Each project
	 * is build, with it's Activities and dependencies added, and the result is
	 * populated in the projectList static variable.
	 * 
	 */
	public static void loadFromDB() {
		// query user_project_relationship
		// get all projID where userID = currentUserID

		Connection connection = null;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection(dataBase);

			// get project members
			PreparedStatement psTotMembers = connection
					.prepareStatement("SELECT * FROM users where user_type = 'MEMBER';");
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

			ps = connection.prepareStatement("SELECT * FROM projects " + "WHERE manager_id = ?;");
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

				ArrayList<Activities> activityList = new ArrayList<Activities>();

				// query activity relation table to get activities associated
				// with project
				PreparedStatement ps4 = connection.prepareStatement(
						"SELECT activity_id FROM activity_project_relationships WHERE " + "project_id = ?");
				ps4.setInt(1, projectID);
				ResultSet result4 = ps4.executeQuery();

				while (result4.next()) {
					// have all activities associated with project

					PreparedStatement ps5 = connection.prepareStatement("SELECT * FROM activities WHERE " + "id = ?");
					ps5.setInt(1, result4.getInt(1));
					ResultSet result5 = ps5.executeQuery();

					while (result5.next()) {
						// now create activities and add to activityList
						int id = result5.getInt(1);
						String name = result5.getString(2);
						String desc = result5.getString(3);
						int duration = result5.getInt(4);

						activityList.add(new Activities(desc, duration, name, id));
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
					// make db call
					PreparedStatement ps5 = connection.prepareStatement(
							"SELECT to_activity_id FROM activity_edge_relationship WHERE " + "from_activity_id = ?");
					ps5.setInt(1, activity.getId());
					ResultSet result5 = ps5.executeQuery();
					while (result5.next()) {
						for (Activities dependent_activity : activityList) {
							if (dependent_activity.getId() == result5.getInt(1)) {
								project.addArrow(activity, dependent_activity);
							}
						}
					}
					
					PreparedStatement ps6 = connection.prepareStatement("SELECT user_id from activity_user_project_relationships where activity_id = ?");
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
			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}

	}

	/**
	 * This method is used to save changes to the database. The current instance
	 * of projectList, which contains all changes the user has made, is
	 * iterated. All new values are inserted, and any changed values replace
	 * their associated tuples. Loops through projectList and inserts projects,
	 * users, activities, dependencies to database.
	 */
	public static void saveToDB() {
		Connection connection = null;
		PreparedStatement statement;

		try {
			connection = DriverManager.getConnection(dataBase);

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
				statement = connection.prepareStatement(sql);
				statement.setInt(1, projectID);
				statement.setString(2, projectName);
				statement.setString(3, date);
				statement.setString(4, description);
				statement.setDouble(5, budget);
				statement.setInt(6, managerID);
				statement.executeUpdate();

				int userID;
				// for each project, insert the list of users associated with
				// that project into the database
				for (Users user : projects.getUserList()) {
					userID = user.getID();
					sql = ("INSERT OR REPLACE INTO user_project_relationships(project_id, user_id) VALUES " + "(?, ?)");
					statement = connection.prepareStatement(sql);
					statement.setInt(1, projectID);
					statement.setInt(2, userID);
					statement.executeUpdate();
				}

				// for each project, insert the list of activities associated
				// with that project into the database
				int activityID, dependentActivityID;
				double duration;
				String actLabel, actDescription;

				for (Activities activity : projects.getActivityList()) {
					activityID = activity.getId();
					actLabel = activity.getLabel();
					actDescription = activity.getDescription();
					duration = activity.getDuration();

					sql = ("INSERT OR REPLACE INTO activities(id, label, description, duration) VALUES "
							+ "(?, ?, ?, ?)");
					statement = connection.prepareStatement(sql);
					statement.setInt(1, activityID);
					statement.setString(2, actLabel);
					statement.setString(3, actDescription);
					statement.setDouble(4, duration);
					statement.executeUpdate();

					sql = ("INSERT OR REPLACE INTO activity_project_relationships(project_id, activity_id) VALUES "
							+ "(?, ?)");
					statement = connection.prepareStatement(sql);
					statement.setInt(1, projectID);
					statement.setInt(2, activityID);
					statement.executeUpdate();

					int memberID;

					for (Users member : activity.getMemberList()) {
						memberID = member.getID();

						sql = ("INSERT OR REPLACE INTO activity_user_project_relationships(activity_id, user_id, project_id) VALUES "
								+ "(?, ?, ?)");
						statement = connection.prepareStatement(sql);
						statement.setInt(1, activityID);
						statement.setInt(2, memberID);
						statement.setInt(3, projectID);
						statement.executeUpdate();
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
							statement = connection.prepareStatement(sql);
							statement.setInt(1, activityID);
							statement.setInt(2, dependentActivityID);
							statement.executeUpdate();
						}
					}
				}

			}

		} catch (Exception exception) {
			System.out.println(exception.getMessage());
		}

		// close connection at end
		try {
			connection.close();
		} catch (Exception closingException) {
			System.out.println(closingException.getMessage());
		}
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

}