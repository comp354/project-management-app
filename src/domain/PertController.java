package domain;

import java.util.ArrayList;

import resources.Activities;
import resources.PertData;
import resources.Projects;
import saver_loader.DataResource;

public class PertController {
	
	
	public static ArrayList<PertData> getPertData(Projects project){
		
		ArrayList<PertData> data = new ArrayList<PertData>();
		ArrayList<Activities> activitiesList = project.getActivityList();
		
		for(Activities a : activitiesList){
			
			data.add(getPertData(a));	
		}
		
		return data;
	}
	

	public static PertData getPertData(Activities Activity){
		
		return DataResource.getPertData(Activity);
		
	}
	
	public static void addPertData(PertData pert){
		
		DataResource.setPertData(pert);
		
	}
	
	
	

}
