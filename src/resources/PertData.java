package resources;

public class PertData {
	
	private int activityId;
	private int pestimisticTime;
	private int optimisticTime;
	private int mostLikelyTime;
	private double standardDeviation;
	
	public PertData(){
		
		this.pestimisticTime = (Integer) null;
		this.optimisticTime = (Integer) null;
		this.mostLikelyTime = (Integer) null;
		this.standardDeviation = (Double) null;
		this.activityId = (Integer) null;
		
	}
	
	
	public PertData(int ativityId,int pestimisticTime, int optimisticTime, int mostLikelyTime, double standardDeviation) {
		this.pestimisticTime = pestimisticTime;
		this.optimisticTime = optimisticTime;
		this.mostLikelyTime = mostLikelyTime;
		this.activityId = ativityId;
		this.standardDeviation = standardDeviation;
	}


	public int getPestimisticTime() {
		return pestimisticTime;
	}


	public void setPestimisticTime(int pestimisticTime) {
		this.pestimisticTime = pestimisticTime;
	}


	public int getOptimisticTime() {
		return optimisticTime;
	}


	public void setOptimisticTime(int optimisticTime) {
		this.optimisticTime = optimisticTime;
	}


	public int getMostLikelyTime() {
		return mostLikelyTime;
	}


	public void setMostLikelyTime(int mostLikelyTime) {
		this.mostLikelyTime = mostLikelyTime;
	}


	public double getStandardDeviation() {
		return standardDeviation;
	}


	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}


	public int getActivityId() {
		return activityId;
	}


	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

}
