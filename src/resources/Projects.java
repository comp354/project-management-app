package resources;

import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultDirectedGraph;

import saver_loader.DataResource;

import java.util.ArrayList;

public class Projects {

	private static int projectCount = 0;
	private int id;
	private String projectName;
	private ArrayList<Users> userList;
	private String date;
	private DefaultDirectedGraph<Activities,DefaultEdge> activityGraph;
	
	

	private ArrayList<Activities> activityList;
	private int managerID;
	private double budget;
	private String description;

	
		
	public Projects(String projectName, ArrayList<Users> userList, String date, int managerID,
			String description, double budget) {
		this.id = ++projectCount;
		this.projectName = projectName;
		this.userList = userList;
		this.date = date;
		this.activityGraph = new DefaultDirectedGraph<Activities,DefaultEdge>(DefaultEdge.class);
		this.activityList = new ArrayList<Activities>();
		this.managerID = managerID;
		this.budget = budget;
		this.description = description;
	}

	public Projects(String projectName, ArrayList<Users> userList, String date, int projectID, int managerID,
			String description, double budget) {
		this.projectName = projectName;
		this.userList = userList;
		this.date = date;
		this.activityGraph = new DefaultDirectedGraph<Activities,DefaultEdge>(DefaultEdge.class);
		this.activityList = new ArrayList<Activities>();
		this.managerID = managerID;
		this.budget = budget;
		this.description = description;
		this.id = projectID;
	}
	
	public ArrayList<Users> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<Users> userList) {
		this.userList = userList;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static int getProjectCount() {
		return projectCount;
	}

	public static void setProjectCount(int projectCount) {
		Projects.projectCount = projectCount;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public DefaultDirectedGraph<Activities, DefaultEdge> getActivityGraph() {
		return activityGraph;
	}

	public void setActivityGraph(DefaultDirectedGraph<Activities, DefaultEdge> activityGraph) {
		this.activityGraph = activityGraph;
	}
	
	/**
	 * @return the activityList
	 */
	public ArrayList<Activities> getActivityList() {
		return activityList;
	}

	/**
	 * @param activityList the activityList to set
	 */
	public void setActivityList(ArrayList<Activities> activityList) {
		this.activityList = activityList;
	}

	public void addActivity(Activities A) {
		this.activityGraph.addVertex(A);
		this.activityList.add(A);
	}
	
	public void addArrow(Activities A, Activities B) {
		this.activityGraph.addEdge(A, B);
	}
	
	public Set<Activities> getActivitySet() {
		return this.activityGraph.vertexSet();
	}
	
	public Set<DefaultEdge> getArrowSet() {
		return this.activityGraph.edgeSet();
	}
	
	public Set<DefaultEdge> getIncomingArrowsOfActivity(Activities A) {
		return this.activityGraph.incomingEdgesOf(A);
	}
	
	public Set<DefaultEdge> getOutgoingArrowsOfActivity(Activities A) {
		return this.activityGraph.outgoingEdgesOf(A);
	}
	
	public Activities getActivityBefore(DefaultEdge e) {
		return this.activityGraph.getEdgeSource(e);
	}
	
	public Activities getActivityAfter(DefaultEdge e) {
		return this.activityGraph.getEdgeTarget(e);
	}
	
	public void setES(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setEarliestStart(n);
		}
	}
	
	public double getES(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getEarliestStart();
		}
		return -1;
	}
	
	public void setEF(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setEarliestFinish(n);
		}
	}
	
	public double getEF(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getEarliestFinish();
		}
		return -1;
	}
	
	public void setLS(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setLatestStart(n);
		}
	}
	
	public double getLS(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getLatestStart();
		}
		return -1;
	}
	
	public void setLF(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setLatestFinish(n);
		}
	}
	
	public double getLF(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getLatestFinish();
		}
		return -1;
	}
	
	public void setFloat(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setActivityFloat(n);
		}
	}
	
	public double getFloat(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getActivityFloat();
		}
		return -1;
	}
	
	public void setMaxDuration(Activities A, double n) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				a.setMaxDuration(n);
		}
	}
	
	public double getMaxDuration(Activities A) {
		for(Activities a : this.activityList)
		{
			if (a.getId() == A.getId())
				return a.getMaxDuration();
		}
		return -1;
	}
	
	public Activities getActivityByLabel(String l){
		for(Activities a : this.activityList)
		{
			if (a.getLabel().equals(l))
				return a;
		}
		return null;
	}
	
	public void deleteActivity(Activities A) {
		this.activityGraph.removeVertex(A);
		this.activityList.remove(A);
		DataResource.deleteActivity(A);
	}
	
	public void resetIncomingEdges(Activities A) {
		Set<DefaultEdge> incomingEdges = this.getIncomingArrowsOfActivity(A);
		
		for(DefaultEdge e : incomingEdges)
		{
			this.activityGraph.removeEdge(e);
			Activities beforeEdge = this.getActivityBefore(e);
			Activities afterEdge = this.getActivityAfter(e);
			DataResource.deleteEdgeFromDB(beforeEdge.getId(), afterEdge.getId());			
		}
	}
	
	public void calculateTimes() {
		Set<Activities> vertexList = getActivitySet();
		Set<DefaultEdge> edgeList = getArrowSet();		
		
		// forward pass
		for (Activities i : vertexList)
		{
			// check if activity is a "first level" activity, no incoming edges
			if (this.activityGraph.inDegreeOf(i) == 0)
			{
				setES(i, 0);
				setEF(i, i.getDuration());	
			}
			else
			{
				Set<DefaultEdge> inEdges = getIncomingArrowsOfActivity(i);
				double highestEF = 0;
				for (DefaultEdge e : inEdges)
				{
					if (getActivityBefore(e).getEarliestFinish() >= highestEF)
						highestEF = getActivityBefore(e).getEarliestFinish();
				}
				setES(i, highestEF);
				setEF(i, highestEF + i.getDuration());
			}
			
		}
		
		// backward pass
		
		// float
		
		// critial path
		
		// max duration
		
	}
	
	
	
}