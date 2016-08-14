package domain;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Test;

import resources.Activities;
import resources.Projects;
import resources.TaskProgress;
import resources.UserType;
import resources.Users;
import saver_loader.DataResource;

public class ProjectControllerTests {
	private static String testDB = "jdbc:sqlite:ultimate_sandwich_test.db";
	
	private Projects project = null;
	
	/**
	 * @Before runs before each test. Sets the test database as the target database, empties it, and sets it up
	 * with the initial conditions for each test. Lastly, the current projectList is cleared from memory.
	 */

	@Before
    public void setUp() {
		
		DataResource.setDatabase(testDB);
		
		tearDownTestDatabase();
    	setUpTestDatabase();

		DataResource.projectList.clear();

		DataResource.currentUser = DataResource.getUserByIdFromDB(1);
		DataResource.selectedProject = DataResource.getProjectByProjectIdFromDB(1);
    }
	
	// Boundary Test
	@Test
    public void budgetShouldBeBetween0and1000000() {		
        // Assert statements
        assertTrue("Budget must accept 500000.", ProjectController.editProject("boundaryTest1", "boundaryTest1", 500000));
        assertTrue("Budget must accept 0.", ProjectController.editProject("boundaryTest2", "boundaryTest2", 0));
        assertTrue("Budget must accept 1.", ProjectController.editProject("boundaryTest3", "boundaryTest3", 1));
        assertTrue("Budget must accept 999999.", ProjectController.editProject("boundaryTest4", "boundaryTest4", 999999));
        assertTrue("Budget must accept 1000000.", ProjectController.editProject("boundaryTest5", "boundaryTest5", 1000000));
    }
	
	// Basis-Path Coverage
	@Test
    public void BasisPathCoverage() {
        // Assert statements
		// Path 1
        assertTrue("Must complete path 1 successfully.", ProjectController.editProject("path1Test1", "path1Test1", 500000));
        // Path 3
        assertFalse("Must complete path 3 successfully.", ProjectController.editProject("path1Test3", "path1Test3", -1));
        // Path 4
        assertFalse("Must complete path 4 successfully.", ProjectController.editProject("", "", 500000));
        // Path 2
		DataResource.selectedProject = null;
        assertFalse("Must complete path 2 successfully.", ProjectController.editProject("path1Test2", "path1Test2", 500000));
    }
	
	private void setUpTestDatabase() {
		// set up the database with test data
		Connection connection = DataResource.createConnectionToDB(testDB);
		String sql = "";
		try{
	    	
			Statement stmt = connection.createStatement();
	    	
	    	System.out.println("connecting to db");
	    	
	    	//create a test user that is a manager
	    	sql = ("INSERT OR REPLACE INTO users(id, first_name, last_name, username, password, user_type) " +
	    	"VALUES (1, 'Turkey', 'Sandwhich', 'tsand', '123', 'MANAGER')");
	    	stmt.executeUpdate(sql);
	    	
	    	//create a test user that is a member
	    	sql = ("INSERT OR REPLACE INTO users(id, first_name, last_name, username, password, user_type) " +
	    	"VALUES (2, 'Chicken', 'Sandwhich', 'csand', '123', 'MEMBER')");
	    	stmt.executeUpdate(sql);
	    	
	    	//create a test user that is a member
	    	sql = ("INSERT OR REPLACE INTO users(id, first_name, last_name, username, password, user_type) " +
	    	"VALUES (3, 'BLT', 'Sandwhich', 'bsand', '123', 'MEMBER')");
	    	stmt.executeUpdate(sql);
	    	
	    	System.out.println("created test users");
	    	
	    	//create project
	    	sql = ("INSERT OR REPLACE INTO projects(id, name, date, description, budget, manager_id) " +
	    	"VALUES (1, 'TestProject', '07-22-2016', 'Just a test', 100.00, '1')");
	    	stmt.executeUpdate(sql);
	    	
	    	System.out.println("created test project");
	    	
	    	//relate the users to the project
			sql = ("INSERT OR REPLACE INTO user_project_relationships(project_id, user_id) " +
			"VALUES (1, 1), (1,2), (1,3)");
			stmt.executeUpdate(sql);
			
			System.out.println("related users to project");
	    	
	    	//create activities
			sql = ("INSERT OR REPLACE INTO activities(id, label, description, startdate, endate) " +
	    	"VALUES (1, 'TestActivity 1', 'Just testing an activity', '07-22-2016', '07-22-2016')");
			stmt.executeUpdate(sql);
			
			sql = ("INSERT OR REPLACE INTO activities(id, label, description, startdate, endate) " +
	    	"VALUES (2, 'TestActivity 2', 'Just testing an activity', '07-22-2016', '07-22-2016')");
			stmt.executeUpdate(sql);
					
			sql = ("INSERT OR REPLACE INTO activities(id, label, description, startdate, endate) " +
	    	"VALUES (3, 'TestActivity 3', 'Just testing an activity', '07-22-2016', '07-22-2016')");
			stmt.executeUpdate(sql);
			
			System.out.println("created activities");
			
			//relate the activities to the project
			sql = ("INSERT OR REPLACE INTO activity_project_relationships(project_id, activity_id) " +
			"VALUES (1, 1), (1,2), (1,3)");
			stmt.executeUpdate(sql);
			System.out.println("related activities to project");
			
			//create edges between activities for later testing
			sql = ("INSERT OR REPLACE INTO activity_edge_relationship(from_activity_id, to_activity_id) " +
			"VALUES (1, 2), (1,3)");
			stmt.executeUpdate(sql);
			System.out.println("created edges between activities");
			
			//relate the members to the activities
			sql = ("INSERT OR REPLACE INTO activity_user_project_relationships(activity_id, user_id, project_id) " +
					"VALUES (1,2,1), (2,2,1), (3,1,1)");
			stmt.executeUpdate(sql);
			System.out.println("related members to activities");
			

		}catch(Exception exception) {
	    	System.out.println(exception.getMessage());
	    }
		
		DataResource.closeConnection(connection);
	}
	
	private void tearDownTestDatabase() {

		Connection connection = DataResource.createConnectionToDB(testDB);
		String sql = "";
		try{
	    	
	    	Statement stmt = connection.createStatement();
	    	
	    	System.out.println("connecting to db");
	    	
	    	//delete all associations
			sql = ("DELETE FROM user_project_relationships");
			stmt.executeUpdate(sql);
			
			sql = ("DELETE FROM activity_project_relationships");
			stmt.executeUpdate(sql);
			
			sql = ("DELETE FROM activity_user_project_relationships");
			stmt.executeUpdate(sql);
			
			sql = ("DELETE FROM activity_edge_relationship");
			stmt.executeUpdate(sql);
			
	    	//delete user
			sql = ("DELETE FROM users");
	    	stmt.executeUpdate(sql);
	    	
	    	//delete project
	    	sql = ("DELETE FROM projects");
	    	stmt.executeUpdate(sql);
	    	
	    	//delete activity
	    	sql = ("DELETE FROM activities");
	    	stmt.executeUpdate(sql);
		
		}catch(Exception exception) {
	    	System.out.println(exception.getMessage());
	    }
		System.out.println("Successfully tore down database.");
		DataResource.closeConnection(connection);
	}
}


