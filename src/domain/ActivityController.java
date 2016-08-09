package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

import resources.Activities;
import resources.TaskProgress;
import resources.Users;
import saver_loader.DataResource;


public class ActivityController extends ActivitySubject{
	ActivityController() {

	}
	
	public static void addActivity(String description, String startDate, String endDate, String label, ArrayList<String> dependencies, ArrayList<String> members, String progress, int budget,boolean specifyPert) {
		try {
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			Date start = dateFormatter.parse(startDate);
			Date end = dateFormatter.parse(endDate);

			Activities newActivity;
			
            if (start.before(end))
            {
                // Create activity and add it to current Project
          
                newActivity = new Activities(description, start, end, label, TaskProgress.valueOf(progress), budget, specifyPert);
            	DataResource.selectedProject.addActivity(newActivity);
               
                // Set the dependencies in the JGraphT
                for (String element : dependencies) {

                    ArrayList<Activities> activities = DataResource.selectedProject.getActivityList();

                    for (Activities activity : activities) {

                        if (activity.getLabel().equals(element))
                            DataResource.selectedProject.addArrow(activity, newActivity);
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
                newActivity.setMemberList(tmp);

                //***************************** SAVE NEW ACTIVITY TO DATABASE **********************
                DataResource.saveActivity(newActivity);

                notifyObservers();
            }
            else
            {
                if (start.after(end))
                {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "End date must be AFTER start date",
                            "Incorrect Dates",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "Please Fill in all values correctly",
                            "Values are incorrect format or missing",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

		} catch (Exception exception) {

            JOptionPane.showMessageDialog(new JFrame(),
                    "Please Fill in all values correctly",
                    "Values are incorrect format or missing",
                    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static void editActivity(String description, String startDate, String endDate, String label, ArrayList<String> dependencies, ArrayList<String> members, String progress, int budget) {		
		try {
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			Date start = dateFormatter.parse(startDate);
			Date end = dateFormatter.parse(endDate);
			
			Activities myActivity = DataResource.selectedActivity;

            if (start.before(end))
            {
                myActivity.setDescription(description);
                myActivity.setStartDate(start);
                myActivity.setEndDate(end);
                myActivity.setLabel(label);
                myActivity.setProgress(TaskProgress.valueOf(progress));
                myActivity.setBudget(budget);

                if (!dependencies.isEmpty()) {                	
                	String firstNode = new String();
                	String lastNode = new String();
                	for(Activities a : DataResource.selectedProject.getActivityList()) {
                		if(DataResource.selectedProject.getOutgoingArrowsOfActivity(a).size() == 0)
                			lastNode = a.getLabel();
                		else if(DataResource.selectedProject.getIncomingArrowsOfActivity(a).size() == 0)
                			firstNode = a.getLabel();
                	}
                	
                	if(myActivity.getLabel().equals(firstNode)) {
                		JOptionPane.showMessageDialog(new JFrame(),
                                "Cannot add dependencies to the first activity",
                                "Incorrect Dependency",
                                JOptionPane.WARNING_MESSAGE);
                		return;
            		}
                	
                	if(dependencies.contains(lastNode)) {
                		JOptionPane.showMessageDialog(new JFrame(),
                                "Activity cannot depend on the last activity",
                                "Incorrect Dependency",
                                JOptionPane.WARNING_MESSAGE);
                		return;
            		}

                    DataResource.selectedProject.resetIncomingEdges(myActivity);
                    ArrayList<Activities> activities = DataResource.selectedProject.getActivityList();

                    // Set the dependencies in the JGraphT
                    for (String element : dependencies) {

                        for (Activities activity : activities) {

                            if (activity.getLabel().equals(element))
                                DataResource.selectedProject.addArrow(activity, myActivity);
                        }
                    }
                }

                if (!members.isEmpty()) {
                    DataResource.resetActivityMembers(DataResource.selectedActivity.getId());
                    ArrayList<Users> users = DataResource.projectMembers;
                    ArrayList<Users> tmp = new ArrayList<Users>();

                    for (String element : members) {
                        for (Users user : users) {
                            if (user.getName().equals(element))
                                tmp.add(user);
                        }
                    }
                    DataResource.selectedActivity.setMemberList(tmp);
                }


                //******************************SAVE TO DATABASE METHOD*********************************8
                DataResource.saveActivity(DataResource.selectedActivity);

                notifyObservers();
            }
            else
            {
                if (start.after(end))
                {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "End date must be AFTER start date",
                            "Incorrect Dates",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(new JFrame(),
                            "End date must be AFTER start date",
                            "Incorrect Dates",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

		} catch (Exception exception) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Please Fill in all values",
                    "Empty Values",
                    JOptionPane.WARNING_MESSAGE);
        }

	}
	
	public static void deleteActivity() {
		Activities myActivity = DataResource.selectedActivity;
		DataResource.selectedProject.deleteActivity(myActivity);
		
		notifyObservers();
	}
}
