package resources;

import java.util.ArrayList;

/**
 * Activities class:
 * 
 * This class represents Activities which are contained in Projects.
 * Activities can be added and removed from Project objects.
 * Activities have an id, label, description, duration, xpos, ypos, earliestStart, earliestFinish, lastestStart, latestFinish, activityFloat and maxDuration.
 * For the time being, only id, label, description and duration are used for current functionalities.
 * xpos and ypos are values to represent the position of the objects when displayed graphically.
 * The other attributes will be used for PERT analysis in future iterations.
 * Activities hold a static activityCount variable which is automatically incremented and used to set id when new objects are created.
 * This static variable is set based on the highest activity id stored in the database, and ensures that id is always unique.
 *  
 * @author daveT
 *
 */

public class Activities {
	
	private static int activityCount = 0;
	private int id;
	private String label;
	private String description;
	private double duration;
	private double xpos, ypos;
	private double earliestStart, earliestFinish, latestStart, latestFinish, activityFloat, maxDuration;
	private ArrayList<Users> memberList;
	
	/**
	 * Default Constructor. 
	 * Sets all values to null or junk values.
	 */
	public Activities() {
		this.id = -1;
		this.description = null;
		this.duration = -1;
		this.xpos = -1;
		this.ypos = -1;
		this.earliestStart = -1;
		this.earliestFinish = -1;
		this.latestStart = -1;
		this.latestFinish = -1;
		this.activityFloat = -1;
		this.maxDuration = -1;
		this.memberList = null;
	}
	
	/**
	 * Parameterized constructor for creating new Activities.
	 * Value for id is set to the automatically incremented activityCount static variable (value initialized on load from database)
	 * This ensures that id is always unique.
	 * The currently unused variables are all set to 0.
	 * 
	 * @param description value for description
	 * @param duration value for duration
	 * @param label value for label
	 */
	public Activities(String description, double duration, String label) {
		this.id = ++activityCount;
		this.description = description;
		this.duration = duration;
		this.label = label;
		this.earliestStart = 0;
		this.earliestFinish = 0;
		this.latestStart = 0;
		this.latestFinish = 0;
		this.activityFloat = 0;
		this.maxDuration = 0;
		xpos =0;
		ypos=0;
		this.memberList = new ArrayList<Users>();
	}
	
	/**
	 * Parameterized constructor for creating Activities from values in database.
	 * Not be used when creating NEW Activities, but rather for creating Activities existing in the database.
	 * Does not automatically set id attribute.
	 * 
	 * @param description value for description
	 * @param duration value for duration
	 * @param label value for label
	 * @param id value for id
	 */
	public Activities(String description, double duration, String label, int id) {
		this.id = id;
		this.description = description;
		this.duration = duration;
		this.label = label;
		this.earliestStart = 0;
		this.earliestFinish = 0;
		this.latestStart = 0;
		this.latestFinish = 0;
		this.activityFloat = 0;
		this.maxDuration = 0;
		xpos =0;
		ypos=0;
		this.memberList = new ArrayList<Users>();
	}

	/**
	 * Getter for label
	 * @return String label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setter for label
	 * @param label value for label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter for activitCount
	 * @return int activityCount
	 */
	public static int getActivityCount() {
		return activityCount;
	}

	/**
	 * Setter for activityCount
	 * @param activityCount value for activityCount
	 */
	public static void setActivityCount(int activityCount) {
		Activities.activityCount = activityCount;
	}

	/**
	 * Getter for id
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter for id
	 * @param id value for id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for description
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * @param description value for description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for duration
	 * @return double duration
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Setter for duration
	 * @param duration value for duration
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * Getter for xPos
	 * @return double xPos
	 */
	public double getXpos() {
		return xpos;
	}

	/**
	 * Setter for xPos
	 * @param xpos value for xPos
	 */
	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	/**
	 * Getter for yPos
	 * @return double yPos
	 */
	public double getYpos() {
		return ypos;
	}

	/**
	 * Setter for yPos
	 * @param ypos value for yPos
	 */
	public void setYpos(double ypos) {
		this.ypos = ypos;
	}

	/**
	 * Getter for earliestStart
	 * @return the earliestStart
	 */
	public double getEarliestStart() {
		return earliestStart;
	}

	/**
	 * Setter for earliestStart
	 * @param earliestStart the earliestStart to set
	 */
	public void setEarliestStart(double earliestStart) {
		this.earliestStart = earliestStart;
	}

	/**
	 * Getter for earliestFinish
	 * @return the earliestFinish
	 */
	public double getEarliestFinish() {
		return earliestFinish;
	}

	/**
	 * Setter for earliestFinish
	 * @param earliestFinish the earliestFinish to set
	 */
	public void setEarliestFinish(double earliestFinish) {
		this.earliestFinish = earliestFinish;
	}

	/**
	 * Getter for latestStart
	 * @return the latestStart
	 */
	public double getLatestStart() {
		return latestStart;
	}

	/**
	 * Setter for latestStart
	 * @param latestStart the latestStart to set
	 */
	public void setLatestStart(double latestStart) {
		this.latestStart = latestStart;
	}

	/**
	 * Getter for latestFinish
	 * @return the latestFinish
	 */
	public double getLatestFinish() {
		return latestFinish;
	}

	/**
	 * Setter for latestFinish
	 * @param latestFinish the latestFinish to set
	 */
	public void setLatestFinish(double latestFinish) {
		this.latestFinish = latestFinish;
	}

	/**
	 * Getter for activityFloat
	 * @return the activityFloat
	 */
	public double getActivityFloat() {
		return activityFloat;
	}

	/**
	 * Setter for activityFloat
	 * @param activityFloat the activityFloat to set
	 */
	public void setActivityFloat(double activityFloat) {
		this.activityFloat = activityFloat;
	}

	/**
	 * Getter for maxDuration
	 * @return the maxDuration
	 */
	public double getMaxDuration() {
		return maxDuration;
	}

	/**
	 * Setter for maxDuration
	 * @param maxDuration the maxDuration to set
	 */
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	/**
	 * Getter for memberList
	 * @return the memberList
	 */
	public ArrayList<Users> getMemberList() {
		return memberList;
	}

	/**
	 * Setter for activityList
	 * @param activityList the activityList to set
	 */
	public void setMemberList(ArrayList<Users> memberList) {
		this.memberList = memberList;
	}
}
