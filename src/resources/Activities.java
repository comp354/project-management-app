package resources;

import java.util.ArrayList;
import java.util.Date;

/**
 * Activities class:
 * 
 * This class represents Activities which are contained in Projects. Activities
 * can be added and removed from Project objects. Activities have an id, label,
 * description, duration, xpos, ypos, earliestStart, earliestFinish,
 * lastestStart, latestFinish, activityFloat and maxDuration. For the time
 * being, only id, label, description and duration are used for current
 * functionalities. xpos and ypos are values to represent the position of the
 * objects when displayed graphically. The other attributes will be used for
 * PERT analysis in future iterations. Activities hold a static activityCount
 * variable which is automatically incremented and used to set id when new
 * objects are created. This static variable is set based on the highest
 * activity id stored in the database, and ensures that id is always unique.
 * 
 * @author daveT
 *
 */

public class Activities {

	private static int activityCount = 0;
	private int id;
	private String label;
	private String description;
	private Date startDate;
	private Date endDate;
	private double xpos, ypos;
	private double earliestStart, earliestFinish, latestStart, latestFinish, activityFloat, maxDuration;
	private int depth;
	private int pv;
	private double ev;
	private ArrayList<Users> memberList;
	private TaskProgress progress;
	private boolean generatedPert;

	/**
	 * Default Constructor. Sets all values to null or junk values.
	 */
	public Activities() {
		this.id = -1;
		this.description = null;
		this.startDate = new Date();
		this.endDate = new Date();
		this.xpos = -1;
		this.ypos = -1;
		this.earliestStart = -1;
		this.earliestFinish = -1;
		this.latestStart = -1;
		this.latestFinish = -1;
		this.activityFloat = -1;
		this.maxDuration = -1;
		this.memberList = null;
		this.depth = -1;
		this.progress = TaskProgress.pending;
		this.pv = -1;
		this.ev = -1;
		this.generatedPert = true;
		
	}

	/**
	 * Parameterized constructor for creating new Activities. Value for id is
	 * set to the automatically incremented activityCount static variable (value
	 * initialized on load from database) This ensures that id is always unique.
	 * The currently unused variables are all set to 0.
	 * 
	 * @param description
	 *            value for description
	 * @param startDate
	 *            value for startDate
	 * @param endDate
	 *            value for endDate
	 * @param label
	 *            value for label
	 */
	public Activities(String description, Date startDate, Date endDate, String label, TaskProgress p, int budget, boolean genpert) {
		this.id = ++activityCount;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.label = label;
		this.earliestStart = 0;
		this.earliestFinish = 0;
		this.latestStart = 0;
		this.latestFinish = 0;
		this.activityFloat = 0;
		this.maxDuration = 0;
		xpos = 0;
		ypos = 0;
		this.memberList = new ArrayList<Users>();
		this.depth = -1;
		this.progress = p;
		this.pv = budget;
		this.generatedPert = genpert;
	}

	/**
	 * Parameterized constructor for creating Activities from values in
	 * database. Not be used when creating NEW Activities, but rather for
	 * creating Activities existing in the database. Does not automatically set
	 * id attribute.
	 * 
	 * @param description
	 *            value for description
	 * @param startDate
	 *            value for startDate
	 * @param endDate
	 *            value for endDate
	 * @param label
	 *            value for label
	 * @param id
	 *            value for id
	 * @param progress2 
	 */
	public Activities(String description, Date startDate, Date endDate, String label, int id, TaskProgress p, int budget, boolean genpert) {
		this.id = id;
		this.description = description;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.label = label;
		this.earliestStart = 0;
		this.earliestFinish = 0;
		this.latestStart = 0;
		this.latestFinish = 0;
		this.activityFloat = 0;
		this.maxDuration = 0;
		xpos = 0;
		ypos = 0;
		this.memberList = new ArrayList<Users>();
		this.progress = p;
		this.pv = budget;
		this.generatedPert = genpert;
	}

	/**
	 * Getter for label
	 * 
	 * @return String label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Setter for label
	 * 
	 * @param label
	 *            value for label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Getter for activitCount
	 * 
	 * @return int activityCount
	 */
	public static int getActivityCount() {
		return activityCount;
	}

	/**
	 * Setter for activityCount
	 * 
	 * @param activityCount
	 *            value for activityCount
	 */
	public static void setActivityCount(int activityCount) {
		Activities.activityCount = activityCount;
	}

	/**
	 * Getter for id
	 * 
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            value for id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter for description
	 * 
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 * 
	 * @param description
	 *            value for description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the number of days between the activities start and end dates
	 * 
	 * @return int duration
	 */
	public int getDuration() {
		final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

		int diffInDays = (int) ((this.endDate.getTime() - this.startDate.getTime()) / DAY_IN_MILLIS);
		return diffInDays;
	}

	/**
	 * Getter for startDate
	 * 
	 * @return Date startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter for startDate
	 * 
	 * @param startDate
	 *            value for startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for endDate
	 * 
	 * @return Date endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter for endDate
	 * 
	 * @param endDate
	 *            value for endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for xPos
	 * 
	 * @return double xPos
	 */
	public double getXpos() {
		return xpos;
	}

	/**
	 * Setter for xPos
	 * 
	 * @param xpos
	 *            value for xPos
	 */
	public void setXpos(double xpos) {
		this.xpos = xpos;
	}

	/**
	 * Getter for yPos
	 * 
	 * @return double yPos
	 */
	public double getYpos() {
		return ypos;
	}

	/**
	 * Setter for yPos
	 * 
	 * @param ypos
	 *            value for yPos
	 */
	public void setYpos(double ypos) {
		this.ypos = ypos;
	}

	/**
	 * Getter for earliestStart
	 * 
	 * @return the earliestStart
	 */
	public double getEarliestStart() {
		return earliestStart;
	}

	/**
	 * Setter for earliestStart
	 * 
	 * @param earliestStart
	 *            the earliestStart to set
	 */
	public void setEarliestStart(double earliestStart) {
		this.earliestStart = earliestStart;
	}

	/**
	 * Getter for earliestFinish
	 * 
	 * @return the earliestFinish
	 */
	public double getEarliestFinish() {
		return earliestFinish;
	}

	/**
	 * Setter for earliestFinish
	 * 
	 * @param earliestFinish
	 *            the earliestFinish to set
	 */
	public void setEarliestFinish(double earliestFinish) {
		this.earliestFinish = earliestFinish;
	}

	/**
	 * Getter for latestStart
	 * 
	 * @return the latestStart
	 */
	public double getLatestStart() {
		return latestStart;
	}

	/**
	 * Setter for latestStart
	 * 
	 * @param latestStart
	 *            the latestStart to set
	 */
	public void setLatestStart(double latestStart) {
		this.latestStart = latestStart;
	}

	/**
	 * Getter for latestFinish
	 * 
	 * @return the latestFinish
	 */
	public double getLatestFinish() {
		return latestFinish;
	}

	/**
	 * Setter for latestFinish
	 * 
	 * @param latestFinish
	 *            the latestFinish to set
	 */
	public void setLatestFinish(double latestFinish) {
		this.latestFinish = latestFinish;
	}

	/**
	 * Getter for activityFloat
	 * 
	 * @return the activityFloat
	 */
	public double getActivityFloat() {
		return activityFloat;
	}

	/**
	 * Setter for activityFloat
	 * 
	 * @param activityFloat
	 *            the activityFloat to set
	 */
	public void setActivityFloat(double activityFloat) {
		this.activityFloat = activityFloat;
	}

	/**
	 * Getter for maxDuration
	 * 
	 * @return the maxDuration
	 */
	public double getMaxDuration() {
		return maxDuration;
	}

	/**
	 * Setter for maxDuration
	 * 
	 * @param maxDuration
	 *            the maxDuration to set
	 */
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}

	/**
	 * Getter for memberList
	 * 
	 * @return the memberList
	 */
	public ArrayList<Users> getMemberList() {
		return memberList;
	}

	/**
	 * Setter for activityList
	 * 
	 * @param activityList
	 *            the activityList to set
	 */
	public void setMemberList(ArrayList<Users> memberList) {
		this.memberList = memberList;
	}

	public void setDepth(int d) {
		this.depth = d;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public String toString() {
		return "ES: " + this.earliestStart + " EF: " + this.earliestFinish + " Duration: " + this.getDuration() + " LS: " + this.latestStart + " LF: " + this.latestFinish + " Float: " + this.activityFloat;
	}

	public TaskProgress getProgress() {
		return progress;
	}

	public void setProgress(TaskProgress progress) {
		this.progress = progress;
	}

	public int getBudget() {
		return pv;
	}

	public void setBudget(int budget) {
		this.pv = budget;
	}
	
	public void calculateEarnedValue() {
		switch(this.getProgress()) {
		case complete:
			this.ev = this.pv;
			break;
		case started:
			this.ev = this.pv / 2;
			break;
		default:
			this.ev = 0;
			break;
		
		}
	}
	
	public double getEarnedValue() {
		return this.ev;
	}

	/**
	 * Returns true if System needs to use other activites to create pert data
	 * 
	 * @return
	 */
	public boolean isPertGenerated() {
		return generatedPert;
	}
}
