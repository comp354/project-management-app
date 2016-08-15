
package saver_loader;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.junit.Before;
import org.junit.Test;

import domain.ActivityController;
import domain.ProjectController;
import resources.Activities;
import resources.Projects;
import resources.TaskProgress;
import resources.UserType;
import resources.Users;

public class DataResourceTest {
    
	private static String testDB = "jdbc:sqlite:ultimate_sandwich_test.db";
	
	private Projects project = null;
	private String today, tomorrow;
	private ArrayList<String> memberListEmpty;
	private ArrayList<String> memberList;
	private ArrayList<String> dependenciesEmpty;
	private ArrayList<String> dependencies;
	
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

    }
	// #2 Boundary Test
	@Test
	public void testGetCostPerformanceIndex() {		
		Projects p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		Activities a = new Activities();
		p.addActivity(a);
		a.setProgress(TaskProgress.complete);
		
		a.calculateEarnedValue();
		p.calculateTimes();
		try {
			p.getEarnedValue();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		assertTrue("should have a budget of -1", p.getBudget() == -1);
		try {
			p.getCostPerformanceIndex();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		a.setBudget(0);

		try {
			p.getActualCost();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		try {
			p.getCostPerformanceIndex();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		a.setBudget(1);
		
		try {
			p.getActualCost();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		
		try {
			p.getCostPerformanceIndex();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		a.setBudget(500);
		try {
			p.getActualCost();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		
		try {
			p.getCostPerformanceIndex();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}

		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(0);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(0);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an acutal cost of 0", p.getActualCost() == 0);
		assertTrue("should have a performance index of 0", p.getCostPerformanceIndex() == 0);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		
		a.setBudget(2000);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(1500);
		a.setProgress(TaskProgress.started);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual cost of 2000", p.getActualCost() == 2000);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1.375);
	}
	
	// #2 Basis-Path Coverage
	@Test
	public void testGetCostPerformanceIndexWorstCase() {		
		Projects p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		Activities a = new Activities();
		p.addActivity(a);
		a.setProgress(TaskProgress.complete);
		
		p.setBudget(0);
				
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 1", p.getActualCost() == 1);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 501", p.getActualCost() == 501);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000500", p.getActualCost() == 10000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 20000500", p.getActualCost() == 20000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		p.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(0);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000000", p.getActualCost() == 10000000);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000001", p.getActualCost() == 10000001);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000501", p.getActualCost() == 10000501);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 20000500", p.getActualCost() == 20000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 30000500", p.getActualCost() == 30000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		p.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(0);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000000", p.getActualCost() == 10000000);
		assertTrue("should have a performance index of 0", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000001", p.getActualCost() == 10000001);
		assertTrue("should have a performance index of 500", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000501", p.getActualCost() == 10000501);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 20000500", p.getActualCost() == 20000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 30000500", p.getActualCost() == 30000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		p.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(0);
		assertTrue("should have an actual value of 10000000", p.getActualCost() == 10000000);
		assertTrue("should have a performance index of 0", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000001", p.getActualCost() == 10000001);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000501", p.getActualCost() == 10000501);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 20000500", p.getActualCost() == 20000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 30000500", p.getActualCost() == 30000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		p.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		
		a.setBudget(0);
		assertTrue("should have an actual value of 0", p.getActualCost() == 10000000);
		assertTrue("should have a performance index of 0", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000001", p.getActualCost() == 10000001);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 10000501", p.getActualCost() == 10000501);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 20000500", p.getActualCost() == 20000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
		
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an actual value of 30000500", p.getActualCost() == 30000500);
		assertTrue("should have a performance index of 1", p.getCostPerformanceIndex() == 1);
	}
	
	// #3 Basis-Path Coverage
	@Test
	public void testGetSchedulePerformanceBasisPath() {
		Projects p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		Activities a = new Activities();
		p.addActivity(a);
		a.setProgress(TaskProgress.complete);
		
		a.calculateEarnedValue();
		p.calculateTimes();
		try {
			p.getEarnedValue();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		assertTrue("should have a budget of -1", p.getBudget() == -1);
		try {
			p.getSchedulePerformanceIndex();
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
		}
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 1", p.getEarnedValue() == 1);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(2000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 2000", p.getEarnedValue() == 2000);
		
		p.setBudget(1500);
		assertTrue("should have a budget of 1500", p.getBudget() == 1500);
		assertTrue("should have a performance index of 1.3333333333333333", p.getSchedulePerformanceIndex() == 1.3333333333333333);
	}
	
	// #3 Boundary Test
	@Test
	public void testGetSchedulePerformanceIndexWorstCase() {		
		Projects p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		Activities a = new Activities();
		p.addActivity(a);
		a.setProgress(TaskProgress.complete);
		
		a.setBudget(0);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 0", p.getEarnedValue() == 0);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(1);
		assertTrue("should have a budget of 1", p.getBudget() == 1);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(500);
		assertTrue("should have a budget of 500", p.getBudget() == 500);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(9999999);
		assertTrue("should have a budget of 9999999", p.getBudget() == 9999999);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(10000000);
		assertTrue("should have a budget of 10000000", p.getBudget() == 10000000);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(1);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 1", p.getEarnedValue() == 1);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(1);
		assertTrue("should have a budget of 1", p.getBudget() == 1);
		assertTrue("should have a performance index of 1", p.getSchedulePerformanceIndex() == 1);
		
		p.setBudget(500);
		assertTrue("should have a budget of 500", p.getBudget() == 500);
		assertTrue("should have a performance index of 0.002", p.getSchedulePerformanceIndex() == 0.002);
		
		p.setBudget(9999999);
		assertTrue("should have a budget of 9999999", p.getBudget() == 9999999);
		assertTrue("should have a performance index of 1.00000010000001E-7", p.getSchedulePerformanceIndex() == 1.00000010000001E-7);
		
		p.setBudget(10000000);
		assertTrue("should have a budget of 10000000", p.getBudget() == 10000000);
		assertTrue("should have a performance index of 0.0000001", p.getSchedulePerformanceIndex() == 0.0000001);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(500);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 500", p.getEarnedValue() == 500);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(1);
		assertTrue("should have a budget of 1", p.getBudget() == 1);
		assertTrue("should have a performance index of 500", p.getSchedulePerformanceIndex() == 500);
		
		p.setBudget(500);
		assertTrue("should have a budget of 500", p.getBudget() == 500);
		assertTrue("should have a performance index of 1", p.getSchedulePerformanceIndex() == 1);
		
		p.setBudget(9999999);
		assertTrue("should have a budget of 9999999", p.getBudget() == 9999999);
		assertTrue("should have a performance index of 5.00000050000005E-5", p.getSchedulePerformanceIndex() == 5.00000050000005E-5);
		
		p.setBudget(10000000);
		assertTrue("should have a budget of 10000000", p.getBudget() == 10000000);
		assertTrue("should have a performance index of 0.00005", p.getSchedulePerformanceIndex() == 0.00005);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(9999999);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 9999999", p.getEarnedValue() == 9999999);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(1);
		assertTrue("should have a budget of 1", p.getBudget() == 1);
		assertTrue("should have a performance index of 9999999", p.getSchedulePerformanceIndex() == 9999999);
		
		p.setBudget(500);
		assertTrue("should have a budget of 500", p.getBudget() == 500);
		assertTrue("should have a performance index of 19999.998", p.getSchedulePerformanceIndex() == 19999.998);
		
		p.setBudget(9999999);
		assertTrue("should have a budget of 9999999", p.getBudget() == 9999999);
		assertTrue("should have a performance index of 1", p.getSchedulePerformanceIndex() == 1);
		
		p.setBudget(10000000);
		assertTrue("should have a budget of 10000000", p.getBudget() == 10000000);
		assertTrue("should have a performance index of 0.9999999", p.getSchedulePerformanceIndex() == 0.9999999);
		
		p = new Projects();
		p.setActivityGraph(new DirectedAcyclicGraph<Activities,DefaultEdge>(DefaultEdge.class));
		p.setActivityList(new ArrayList<Activities>());
		p.addActivity(a);
		a.setBudget(10000000);
		a.calculateEarnedValue();
		p.calculateTimes();
		assertTrue("should have an earned value of 10000000", p.getEarnedValue() == 10000000);
		
		p.setBudget(0);
		assertTrue("should have a budget of 0", p.getBudget() == 0);
		assertTrue("should have a performance index of 0", p.getSchedulePerformanceIndex() == 0);
		
		p.setBudget(1);
		assertTrue("should have a budget of 1", p.getBudget() == 1);
		assertTrue("should have a performance index of 10000000", p.getSchedulePerformanceIndex() == 10000000);
		
		p.setBudget(500);
		assertTrue("should have a budget of 500", p.getBudget() == 500);
		assertTrue("should have a performance index of 20000", p.getSchedulePerformanceIndex() == 20000);
		
		p.setBudget(9999999);
		assertTrue("should have a budget of 9999999", p.getBudget() == 9999999);
		assertTrue("should have a performance index of 1.00000010000001", p.getSchedulePerformanceIndex() == 1.00000010000001);
		
		p.setBudget(10000000);
		assertTrue("should have a budget of 10000000", p.getBudget() == 10000000);
		assertTrue("should have a performance index of 1", p.getSchedulePerformanceIndex() == 1);
	}
	
	// Helper for #4 and #5
	private void num4N5Helper() {
		DataResource.currentUser = DataResource.getUserByIdFromDB(1);
		DataResource.selectedProject = DataResource.getProjectByProjectIdFromDB(1);
	}
	
	// Helper for #4
	public void num4Helper() {
		num4N5Helper();
		
		memberListEmpty = new ArrayList<String>();
		memberList = new ArrayList<String>();
		memberList.add("tsand");
		
		dependenciesEmpty = new ArrayList<String>();
		dependencies = new ArrayList<String>();
		dependencies.add("TestActivity 3");
				
		today = "22-07-2016";
		tomorrow = "23-07-2016";
	}
	
	// #4 Boundary Test
	@Test
    public void budgetShouldBeBetween0And100000AlsoTargetDateShouldBeBetween0And1000() {
		num4Helper();
		
        // Assert statements
        assertTrue("Must accept budget of 50000 and target date of 500.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 500));
        assertTrue("Must accept budget of 50000 and target date of 0.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 0));
        assertTrue("Must accept budget of 50000 and target date of 1.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 1));
        assertTrue("Must accept budget of 50000 and target date of 999.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 999));
        assertTrue("Must accept budget of 50000 and target date of 1000.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 1000));
        
        assertTrue("Must accept budget of 0 and target date of 500.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 0, 2, 1, 3, 500));
        assertTrue("Must accept budget of 0 and target date of 0.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 0, 2, 1, 3, 0));
        assertTrue("Must accept budget of 0 and target date of 1.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 0, 2, 1, 3, 1));
        assertTrue("Must accept budget of 0 and target date of 999.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 0, 2, 1, 3, 999));
        assertTrue("Must accept budget of 0 and target date of 1000.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 0, 2, 1, 3, 1000));
        
        assertTrue("Must accept budget of 1 and target date of 500.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 1, 2, 1, 3, 500));
        assertTrue("Must accept budget of 1 and target date of 0.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 1, 2, 1, 3, 0));
        assertTrue("Must accept budget of 1 and target date of 1.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 1, 2, 1, 3, 1));
        assertTrue("Must accept budget of 1 and target date of 999.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 1, 2, 1, 3, 999));
        assertTrue("Must accept budget of 1 and target date of 1000.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 1, 2, 1, 3, 1000));
        
        assertTrue("Must accept budget of 99999 and target date of 500.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 99999, 2, 1, 3, 500));
        assertTrue("Must accept budget of 99999 and target date of 0.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 99999, 2, 1, 3, 0));
        assertTrue("Must accept budget of 99999 and target date of 1.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 99999, 2, 1, 3, 1));
        assertTrue("Must accept budget of 99999 and target date of 999.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 99999, 2, 1, 3, 999));
        assertTrue("Must accept budget of 99999 and target date of 1000.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 99999, 2, 1, 3, 1000));
        
        assertTrue("Must accept budget of 100000 and target date of 500.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 100000, 2, 1, 3, 500));
        assertTrue("Must accept budget of 100000 and target date of 0.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 100000, 2, 1, 3, 0));
        assertTrue("Must accept budget of 100000 and target date of 1.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 100000, 2, 1, 3, 1));
        assertTrue("Must accept budget of 100000 and target date of 999.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 100000, 2, 1, 3, 999));
        assertTrue("Must accept budget of 100000 and target date of 1000.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 100000, 2, 1, 3, 1000));
    }
	
	// #4 Basis-Path Coverage
	@Test
    public void Num4BasisPathCoverage() {
			num4Helper();
			
	        // Assert statements
			// Path 1
	        assertTrue("Must complete path 1 successfully.", ActivityController.addActivity("path1Test1", today, tomorrow, "path1Test1", dependencies, memberList, "pending", 50000, 2, 1, 3, 500));
	        // Path 2
	        assertFalse("Must complete path 2 successfully.", ActivityController.addActivity("path1Test2", "", "", "path1Test2", dependencies, memberList, "pending", 50000, 2, 1, 3, 500));
	        // Path 3
	        assertFalse("Must complete path 3 successfully.", ActivityController.addActivity("path1Test3", today, "", "path1Test3", dependencies, memberList, "pending", 50000, 2, 1, 3, 500));
	        // Path 4
	        assertFalse("Must complete path 4 successfully.", ActivityController.addActivity("path1Test4", today, tomorrow, "path1Test4", dependencies, memberList, "pending", -1, -1, -1, -1, -1));
	        // Path 5
	        assertFalse("Must complete path 5 successfully.", ActivityController.addActivity("path1Test5", tomorrow, today, "path1Test5", dependencies, memberList, "pending", -1, -1, -1, -1, -1));
	        // Path 6
	        assertTrue("Must complete path 6 successfully.", ActivityController.addActivity("path1Test6", today, tomorrow, "path1Test6", dependenciesEmpty, memberList, "pending", 50000, 2, 1, 3, 500));
	        // Path 7
	        assertTrue("Must complete path 7 successfully.", ActivityController.addActivity("path1Test7", today, tomorrow, "path1Test7", dependencies, memberListEmpty, "pending", 50000, 2, 1, 3, 500));
	    }
	
	// #5 Boundary Test
	@Test
    public void budgetShouldBeBetween0and1000000() {	
		num4N5Helper();
		
        // Assert statements
        assertTrue("Budget must accept 500000.", ProjectController.editProject("boundaryTest1", "boundaryTest1", 500000));
        assertTrue("Budget must accept 0.", ProjectController.editProject("boundaryTest2", "boundaryTest2", 0));
        assertTrue("Budget must accept 1.", ProjectController.editProject("boundaryTest3", "boundaryTest3", 1));
        assertTrue("Budget must accept 999999.", ProjectController.editProject("boundaryTest4", "boundaryTest4", 999999));
        assertTrue("Budget must accept 1000000.", ProjectController.editProject("boundaryTest5", "boundaryTest5", 1000000));
    }
	
	// #5 Basis-Path Coverage
	@Test
    public void Num5BasisPathCoverage() {
		num4N5Helper();
		
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
	
	@Test
	public void testGetProjectbyProjectId() {		
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//a project is created in the set up with id 1
		Projects p = DataResource.getProjectbyProjectId(1);
		assertTrue("should have id 1", p.getId() == 1);
		assertTrue("should be named TestProject", p.getProjectName().equals("TestProject"));
		assertTrue("should be 07-22-2016", p.getDate().equals("07-22-2016"));
		assertTrue("should be just a test", p.getDescription().equals("Just a test"));
		assertTrue("should be 100.00", p.getBudget() == 100.00);
		assertTrue("should be 1", p.getManagerID() == 1);	
	}

	@Test
	public void testGetProjectbyProjectName() {
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//a project is created in the set up with name TestProject
		Projects p = DataResource.getProjectbyProjectName("TestProject");
		assertTrue("should have id 1", p.getId() == 1);
		assertTrue("should be named TestProject", p.getProjectName().equals("TestProject"));
		assertTrue("should be 07-22-2016", p.getDate().equals("07-22-2016"));
		assertTrue("should be just a test", p.getDescription().equals("Just a test"));
		assertTrue("should be 100.00", p.getBudget() == 100.00);
		assertTrue("should be 1", p.getManagerID() == 1);		
	}

	@Test
	public void testRemoveProject() {
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		Projects p = DataResource.projectList.get(0);
		
		DataResource.removeProject(p);
		
		assertTrue("should have no projects", DataResource.projectList.size() == 0);
		
		Connection connection = DataResource.createConnectionToDB(testDB);		
		try {
			
			String sql = "SELECT COUNT(*) as total FROM projects";
	    	Statement stmt = connection.createStatement();
	    	System.out.println("connecting to db");
	    	
	    	ResultSet result = stmt.executeQuery(sql);

	    	assertTrue(result.getInt("total") == 0);
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}	
		
		DataResource.closeConnection(connection);
	}

	@Test
	public void testDeleteActivity() {
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		Projects p = DataResource.projectList.get(0);
		Activities a1 = p.getActivityList().get(0);
		Activities a2 = p.getActivityList().get(1);
		Activities a3 = p.getActivityList().get(2);
		
		//delete activities from the project, which automatically calls for their deletion in the database
		p.deleteActivity(a1);
		p.deleteActivity(a2);
		p.deleteActivity(a3);
		
		Connection connection = DataResource.createConnectionToDB(testDB);		
		try {
	
			String sql = "SELECT COUNT(*) as total FROM activities";
	    	Statement stmt = connection.createStatement();
	    	System.out.println("connecting to db");
	    	
	    	ResultSet result = stmt.executeQuery(sql);
	
	    	assertTrue(result.getInt("total") == 0);
		}
		catch(Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		DataResource.closeConnection(connection);
	}

	@Test
	public void testLoadFromDB() {		
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		// test the method
		System.out.println("calling loadFromDb method");
		DataResource.loadManagerDataFromDB();
		System.out.println("called loadFromDb method");
		
		// the resource should have one project in it
		assertTrue("resource should have one project", DataResource.projectList.size() == 1);
		System.out.println("resource size tested");
		
		// make sure the project has the correct attributes
		Projects p = DataResource.projectList.get(0);
		assertTrue("should have id 1", p.getId() == 1);
		assertTrue("should be named TestProject", p.getProjectName().equals("TestProject"));
		assertTrue("should be 07-22-2016", p.getDate().equals("07-22-2016"));
		assertTrue("should be just a test", p.getDescription().equals("Just a test"));
		assertTrue("should be 100.00", p.getBudget() == 100.00);
		assertTrue("should be 1", p.getManagerID() == 1);
		
		System.out.println("project attributes tested");
		
		// make sure it has one activity associated with it
		assertTrue("should have three activities", p.getActivityList().size() == 3);
		
		//create test date objects
		Date startDate = null; 
		Date endDate = null;
		try {
			startDate = DataResource.dateFormatter.parse("07-22-2016");
			endDate = DataResource.dateFormatter.parse("07-22-2016");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// make sure activity attributes are correct
		Activities a1 = p.getActivityList().get(0);
		assertTrue("should have id 1", a1.getId() == 1);
		assertTrue("should have label TestActivity 1", a1.getLabel().equals("TestActivity 1"));
		assertTrue("should have description", a1.getDescription().equals("Just testing an activity"));
		assertTrue("should have a start date of 07-22-2016" + a1.getStartDate(),  a1.getStartDate().equals(startDate));
		assertTrue("should have an end date of 07-22-2016 " + a1.getEndDate(), a1.getEndDate().equals(endDate));
		
		Activities a2 = p.getActivityList().get(1);
		assertTrue("should have id 1", a2.getId() == 2);
		assertTrue("should have label TestActivity 2", a2.getLabel().equals("TestActivity 2"));
		assertTrue("should have description", a2.getDescription().equals("Just testing an activity"));
		assertTrue("should have a start date of 07-22-2016", a2.getStartDate().equals(startDate));
		assertTrue("should have an end date of 07-22-2016", a2.getEndDate().equals(endDate));
		
		Activities a3 = p.getActivityList().get(2);
		assertTrue("should have id 1", a3.getId() == 3);
		assertTrue("should have label TestActivity 3", a3.getLabel().equals("TestActivity 3"));
		assertTrue("should have description", a3.getDescription().equals("Just testing an activity"));
		assertTrue("should have a start date of 07-22-2016", a3.getStartDate().equals(startDate));
		assertTrue("should have an end date of 07-22-2016", a3.getEndDate().equals(endDate));
		
		System.out.println("activity attributes tested");
		
	}
	
	@Test
	public void testEditProject(){
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//get the project to edit
		Projects p = DataResource.getProjectbyProjectId(1);
		
		//edit the project
		p.setBudget(250.00);
		p.setDate("07-25-2016");
		p.setDescription("Changed the test description");
		p.setProjectName("TestProjectEdited");
		
		//save the changes to the test database
		DataResource.saveProject(p);
		
		//load the changes to test if changed correctly
		DataResource.loadManagerDataFromDB();
		
		//get the same project to test if changes were made
		Projects p1 = DataResource.getProjectbyProjectId(1);
		
		assertTrue("budget should equal 250.00", p1.getBudget()==250.00);
		assertTrue("date should equal 07-25-2016", p1.getDate().equals("07-25-2016"));
		assertTrue("description should equal: Changed the test description", p1.getDescription().equals("Changed the test description"));
		assertTrue("project name should equal: TestProjectEdited", p1.getProjectName().equals("TestProjectEdited"));
		
		System.out.println("project editing tested");
	}
	
	@Test
	public void testEditActivities(){
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//get the project to edit
		Projects p = DataResource.getProjectbyProjectId(1);
		DataResource.selectedProject = p;
		
		//get the activities to edit
		Activities a1 = p.getActivityList().get(0);
		Activities a2 = p.getActivityList().get(1);
		Activities a3 = p.getActivityList().get(2);
		
		Date date1 = null;
		try {
			date1 = DataResource.dateFormatter.parse("07-25-2016");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//edit the activities
		a1.setDescription("New Description");
		a2.setDescription("New Description");
		a3.setDescription("New Description");
		a1.setLabel("New Label");
		a2.setLabel("New Label");
		a3.setLabel("New Label");
		a1.setStartDate(date1);
		a2.setStartDate(date1);
		a3.setStartDate(date1);
		a1.setEndDate(date1);
		a2.setEndDate(date1);
		a3.setEndDate(date1);
		
		//save changes to the test database
		DataResource.saveActivity(a1);
		DataResource.saveActivity(a2);
		DataResource.saveActivity(a3);
		
		//load from database to check if changes there
		DataResource.loadManagerDataFromDB();
		
		//get the project and activities to test
		p = DataResource.getProjectbyProjectId(1);
		
		a1 = p.getActivityList().get(0);
		a2 = p.getActivityList().get(1);
		a3 = p.getActivityList().get(2);
		
		assertTrue("should equal New Description",a1.getDescription().equals("New Description"));
		assertTrue("should equal New Label",a1.getLabel().equals("New Label"));
		assertTrue("should equal 07-25-2016",a1.getStartDate().equals(date1));
		assertTrue("should equal 07-25-2016",a1.getEndDate().equals(date1));
		
		assertTrue("should equal New Description",a2.getDescription().equals("New Description"));
		assertTrue("should equal New Label",a2.getLabel().equals("New Label"));
		assertTrue("should equal 07-25-2016",a2.getStartDate().equals(date1));
		assertTrue("should equal 07-25-2016",a2.getEndDate().equals(date1));
		
		assertTrue("should equal New Description",a3.getDescription().equals("New Description"));
		assertTrue("should equal New Label",a3.getLabel().equals("New Label"));
		assertTrue("should equal 07-25-2016",a3.getStartDate().equals(date1));
		assertTrue("should equal 07-25-2016",a3.getEndDate().equals(date1));
		
		System.out.println("tested edit activities");
		
	}
	
	 
	@Test
	public void testAddActivity(){
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		//get the project to edit
		Projects p = DataResource.getProjectbyProjectId(1);
		DataResource.selectedProject = p;
		
		//create test date objects
		Date startDate = null, endDate = null;
		try {
			startDate = DataResource.dateFormatter.parse("07-22-2016");
			endDate = DataResource.dateFormatter.parse("07-23-2016");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create the activity to add
		Activities a = new Activities("New Activity Created", startDate, endDate, "New Label Created", 4, TaskProgress.pending, 0, 0, 0 ,0, 0);
		//add the activity
		p.addActivity(a);
		
		ArrayList<String> dependencies = new ArrayList<String>();
		ArrayList<String> members = new ArrayList<String>();
		
		// Set the dependencies in the JGraphT
					for (String element : dependencies) {

						ArrayList<Activities> activities = DataResource.selectedProject.getActivityList();

						for (Activities activity : activities) {

							if (activity.getLabel().equals(element))
								DataResource.selectedProject.addArrow(activity, a);
						}
					}

					ArrayList<Users> users = DataResource.projectMembers;
					ArrayList<Users> tmp = new ArrayList<Users>();

					for (String element : members) {
						for (Users user : users) {
							if (user.getName().equals(element))
								tmp.add(user);
						}
					}
					a.setMemberList(tmp);
		
		//save the new activity
		DataResource.saveActivity(a);
		
		//load from database to check if changes there
		DataResource.loadManagerDataFromDB();
		p = DataResource.getProjectbyProjectId(1);
		DataResource.selectedProject = p;
		
		Activities newActivity = p.getActivityByLabel("New Label Created");
		
		assertTrue("",newActivity.getLabel().equals("New Label Created"));
		assertTrue("",newActivity.getDescription().equals("New Activity Created"));
		//TODO assert tests for dates
		assertTrue("",newActivity.getStartDate().equals(startDate));
		assertTrue("",newActivity.getEndDate().equals(endDate));
		assertTrue("",newActivity.getId()==4);
		
		System.out.println("tested adding an activity");
		
		
	}
	
	
	
	@Test
	public void testDeleteActivityDependencies(){
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//get the project to edit
		Projects p = DataResource.getProjectbyProjectId(1);
		DataResource.selectedProject = p;
		
		//get the activities to edit
		Activities a1 = p.getActivityList().get(0);
		Activities a2 = p.getActivityList().get(1);
		Activities a3 = p.getActivityList().get(2);
		
		//delete dependency between a1 and a2 and a1 and a3
		p.resetIncomingEdges(a2);
		p.resetIncomingEdges(a3);
		
		//get all the edges in the project
		Set<DefaultEdge> edges = p.getArrowSet();
		
		assertTrue("size of arrow set should be 0",edges.size()==0);
		
		System.out.println("tested delete activity dependencies");

	}

	@Test
	public void testAddActivityDependencies(){
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		//get the project to edit
		Projects p = DataResource.getProjectbyProjectId(1);
		DataResource.selectedProject = p;
		
		//get the activities to edit
		Activities a1 = p.getActivityList().get(0);
		Activities a2 = p.getActivityList().get(1);
		Activities a3 = p.getActivityList().get(2);
		
		//add dependency between a2 and a3
		p.addArrow(a2, a3);
		
		//save to test database
		DataResource.saveProject(p);
		DataResource.saveActivity(a2);
		DataResource.saveActivity(a3);
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		p = DataResource.getProjectbyProjectId(1);
		//get the activities to edit
		a1 = p.getActivityList().get(0);
		a2 = p.getActivityList().get(1);
		a3 = p.getActivityList().get(2);
		
		//test to see if added dependency there
		
		//get the outgoing arrows of a2 and test if a3 is among the activities connected
		Set<DefaultEdge> a2OutGoingEdges = p.getOutgoingArrowsOfActivity(a2);
		
		for(DefaultEdge e: a2OutGoingEdges){
			if(p.getActivityAfter(e).getId()==a3.getId()){
				assertTrue("activity after a2 should equal id of a3",p.getActivityAfter(e).getId()==a3.getId());
			}
		}
		
		//get the incoming arrows of a3 and test if a2 is there
		Set<DefaultEdge> a3IncomingEdges = p.getIncomingArrowsOfActivity(a3);
		
		for(DefaultEdge e: a3IncomingEdges){
			if(p.getActivityBefore(e).getId()==a2.getId()){
				assertTrue("activity before a3 should equal id of a2",p.getActivityBefore(e).getId()==a2.getId());
			}
		}
		
		System.out.println("tested add activity dependencies");
		
	}
	
	@Test
	public void testLoadFromDBMember() {		
		// set current user
		DataResource.currentUser = new Users( "csand", "Chicken", "Sandwhich", "123", 2, "MEMBER");
		
		// test the method
		System.out.println("calling loadFromDb method");
		DataResource.loadMemberDataFromDB();
		System.out.println("called loadFromDb method");
		
		// the resource should have one project in it
		assertTrue("resource should have one project", DataResource.projectList.size() == 1);
		System.out.println("resource size tested");
		
		// make sure the project has the correct attributes
		Projects p = DataResource.projectList.get(0);
		assertTrue("should have id 1", p.getId() == 1);
		assertTrue("should be named TestProject", p.getProjectName().equals("TestProject"));
		assertTrue("should be 07-22-2016", p.getDate().equals("07-22-2016"));
		assertTrue("should be just a test", p.getDescription().equals("Just a test"));
		assertTrue("should be 100.00", p.getBudget() == 100.00);
		assertTrue("should be 1", p.getManagerID() == 1);
		
		System.out.println("project attributes tested");
		
		// make sure it has two activity associated with it
		assertTrue("should have two activities", p.getActivityList().size() == 2);
		
		//create test date objects
		Date startDate = null, endDate = null;
		try {
			startDate = DataResource.dateFormatter.parse("07-22-2016");
			endDate = DataResource.dateFormatter.parse("07-22-2016");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// make sure activity attributes are correct
		Activities a1 = p.getActivityList().get(0);
		assertTrue("should have id 1", a1.getId() == 1);
		assertTrue("should have label TestActivity 1", a1.getLabel().equals("TestActivity 1"));
		assertTrue("should have description", a1.getDescription().equals("Just testing an activity"));
		assertTrue("should have a start date of 07-22-2016", a1.getStartDate().equals(startDate));
		assertTrue("should have an end date of 07-22-2016", a1.getEndDate().equals(endDate));
		
		Activities a2 = p.getActivityList().get(1);
		assertTrue("should have id 1", a2.getId() == 2);
		assertTrue("should have label TestActivity 2", a2.getLabel().equals("TestActivity 2"));
		assertTrue("should have description", a2.getDescription().equals("Just testing an activity"));
		assertTrue("should have a start date of 07-22-2016", a2.getStartDate().equals(startDate));
		assertTrue("should have an end date of 07-22-2016", a2.getEndDate().equals(endDate));
		
				
		System.out.println("activity attributes tested");
		
	}
	
	@Test
	public void testAddMemberToActivity() {
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		Projects p = DataResource.projectList.get(0);
		Activities a1 = p.getActivityList().get(0);
		
		// get the current memberlist
		ArrayList<Users> currentList = a1.getMemberList();
		
		// add a new member
		currentList.add(new Users("ssand", "Salami", "Sandwich", "987", 99, "MEMBER"));
		
		// get the index of the most recent addition
		int index = currentList.size() - 1;
		
		// set the memberlist to this new one
		a1.setMemberList(currentList);
		
		// test to see if member has been added
		assertTrue("should have username ssand", a1.getMemberList().get(index).getName().equals("ssand"));
		assertTrue("should have first name Salami", a1.getMemberList().get(index).getFirstName().equals("Salami"));
		assertTrue("should have last name Sandwich", a1.getMemberList().get(index).getLastName().equals("Sandwich"));
		assertTrue("should have password 987", a1.getMemberList().get(index).getPassword().equals("987"));
		assertTrue("should be 99", a1.getMemberList().get(index).getID() == 99);
		assertTrue("should be member", a1.getMemberList().get(index).getType() == UserType.MEMBER);
	}
	
	@Test
	public void testRemoveMemberFromActivity() {
		// set current user
		DataResource.currentUser = new Users( "tsand", "Turkey", "Sandwhich", "123", 1, "MANAGER");
		
		//loads all projects and activities
		DataResource.loadManagerDataFromDB();
		
		Projects p = DataResource.projectList.get(0);
		Activities a1 = p.getActivityList().get(0);
		
		// get the current memberlist
		ArrayList<Users> currentList = a1.getMemberList();
		
		// remove all elements in the list, all members
		currentList.clear();
		
		// set the memberlist to this modified one one
		a1.setMemberList(currentList);
		
		// test to see if members have been removed
		assertTrue("should be 0", a1.getMemberList().size() == 0);
		
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

