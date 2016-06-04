package resources;

public class Activities {

	private static int activityCount = 0;
	private int id;
	private String label;
	private String description;
	private double duration;
	private double xpos, ypos;
	private double earliestStart, earliestFinish, latestStart, latestFinish, activityFloat, maxDuration;
	
	
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
	}
	
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
	}
	
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
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public static int getActivityCount() {
		return activityCount;
	}

	public static void setActivityCount(int activityCount) {
		Activities.activityCount = activityCount;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getDuration() {
		return duration;
	}


	public void setDuration(double duration) {
		this.duration = duration;
	}


	public double getXpos() {
		return xpos;
	}


	public void setXpos(double xpos) {
		this.xpos = xpos;
	}


	public double getYpos() {
		return ypos;
	}


	public void setYpos(double ypos) {
		this.ypos = ypos;
	}

	/**
	 * @return the earliestStart
	 */
	public double getEarliestStart() {
		return earliestStart;
	}

	/**
	 * @param earliestStart the earliestStart to set
	 */
	public void setEarliestStart(double earliestStart) {
		this.earliestStart = earliestStart;
	}

	/**
	 * @return the earliestFinish
	 */
	public double getEarliestFinish() {
		return earliestFinish;
	}

	/**
	 * @param earliestFinish the earliestFinish to set
	 */
	public void setEarliestFinish(double earliestFinish) {
		this.earliestFinish = earliestFinish;
	}

	/**
	 * @return the latestStart
	 */
	public double getLatestStart() {
		return latestStart;
	}

	/**
	 * @param latestStart the latestStart to set
	 */
	public void setLatestStart(double latestStart) {
		this.latestStart = latestStart;
	}

	/**
	 * @return the latestFinish
	 */
	public double getLatestFinish() {
		return latestFinish;
	}

	/**
	 * @param latestFinish the latestFinish to set
	 */
	public void setLatestFinish(double latestFinish) {
		this.latestFinish = latestFinish;
	}

	/**
	 * @return the activityFloat
	 */
	public double getActivityFloat() {
		return activityFloat;
	}

	/**
	 * @param activityFloat the activityFloat to set
	 */
	public void setActivityFloat(double activityFloat) {
		this.activityFloat = activityFloat;
	}

	/**
	 * @return the maxDuration
	 */
	public double getMaxDuration() {
		return maxDuration;
	}

	/**
	 * @param maxDuration the maxDuration to set
	 */
	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}
	
	
	
	
	
}
