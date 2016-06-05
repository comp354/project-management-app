package listview_components;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import resources.Activities;
import saver_loader.DataResource;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Activity_edit extends JFrame {

	private JPanel contentPane;
	private JTextField descriptionField;
	private JTextField durationField;
	private JTextField activityLabelField;

	private ArrayList<String> dependencies = new ArrayList<String>();
	
	
	public Activity_edit() {
		
		//Initialize JFrame Settings
		setTitle("EDITING");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 426, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Create and add Description Field
		descriptionField = new JTextField( DataResource.selectedActivity.getDescription());
		descriptionField.setBounds(226, 23, 124, 20);
		descriptionField.setColumns(10);
		contentPane.add(descriptionField);

		//Create and add all Labels
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(64, 26, 58, 14);
		contentPane.add(lblDescription);
		
		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(64, 64, 46, 14);
		contentPane.add(lblDuration);
		
		JLabel lblLabel = new JLabel("Label");
		lblLabel.setBounds(64, 103, 46, 14);
		contentPane.add(lblLabel);
		
		JLabel lblDependencies = new JLabel("Dependencies");
		lblDependencies.setBounds(64, 173, 80, 14);
		contentPane.add(lblDependencies);
		
		//Create and add all text Fields
		Double initialDuration =  new Double(DataResource.selectedActivity.getDuration());
		durationField = new JTextField(initialDuration.toString());
		durationField.setBounds(226, 61, 58, 20);
		contentPane.add(durationField);
		durationField.setColumns(10);
		
		activityLabelField = new JTextField(DataResource.selectedActivity.getLabel());
		activityLabelField.setBounds(226, 100, 58, 20);
		contentPane.add(activityLabelField);
		activityLabelField.setColumns(10);
		
		
		//Create an array of the current Activities
		int activityCount = DataResource.selectedProject.getActivityList().size();
		Activities[] activityList = new Activities[activityCount];
		DataResource.selectedProject.getActivityList().toArray(activityList);
		
		//Create Selections from the list of Activities and their labels
		String[] selections = new String[activityCount];
		
		//Get the currently selected dependencies for this activity
		ArrayList<Activities> currentSelections = DataResource.selectedProject.getSetofDependencyActivities(DataResource.selectedActivity);
		
		
		for(int i = 0; i < activityCount; i++){			
			if(!DataResource.selectedActivity.getLabel().equals(activityList[i].getLabel()))
				selections[i] = activityList[i].getLabel();
			}
		
		
		//Create ScrollPane with list inside and add to Frame
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(226, 155, 101, 88);
		
		
		contentPane.add(scrollPane_1);
		
		
		//Initialize and set Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(64, 299, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(238, 299, 89, 23);
		contentPane.add(btnSave);
		
		//Create list with selections
		JList<String> selectionList = new JList<String>(selections);
		selectionList.setBounds(232, 172, 95, 82);
		
		//Set the default selections to current dependent activities
		int[] selectedIndices = new int[currentSelections.size()];
		for(int i = 0; i < currentSelections.size(); i++)
		{
			for(int j = 0; j < activityList.length; j++)
			{
				if(currentSelections.get(i).getId() == activityList[j].getId())
					selectedIndices[i] = j;
			}
		}
		selectionList.setSelectedIndices(selectedIndices);
		contentPane.add(selectionList);
		
				
		//Add to viewport
		scrollPane_1.setViewportView(selectionList);
		
		
		
		
		JLabel lblDoYouWant = new JLabel("Do you want to delete?");
		lblDoYouWant.setBounds(64, 351, 124, 23);
		contentPane.add(lblDoYouWant);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(238, 351, 89, 23);
		contentPane.add(btnDelete);
		
		//Create the listListener for dependency choices
		selectionList.addListSelectionListener(new ListSelectionListener() {
			
			
			public void valueChanged(ListSelectionEvent e) {

				if (e.getValueIsAdjusting()) {//This line prevents double events
	    			
					dependencies = (ArrayList<String>) selectionList.getSelectedValuesList();
					
	    	    }
			}
		});
		
		//Add and define ActionListeners to the buttons
		
		btnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAction();
				ActivityListPane.updateTable(DataResource.selectedProject);
				disposeWindow();				
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				disposeWindow();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
				ActivityListPane.updateTable(DataResource.selectedProject);
				disposeWindow();
			}
		});
		
	}
	
	private void saveAction () {
		

		Activities myActivity = DataResource.selectedActivity;
		
		
		myActivity.setDescription(descriptionField.getText());
		myActivity.setDuration(Double.parseDouble(durationField.getText()));
		myActivity.setLabel(activityLabelField.getText());
		
		if(!dependencies.isEmpty()){
		
			DataResource.selectedProject.resetIncomingEdges(myActivity);
			ArrayList<Activities> activities = DataResource.selectedProject.getActivityList();

		
			//Set the dependencies in the JGraphT
				for(String element : dependencies){
						
					
					for(Activities activity : activities){
						
						if(activity.getLabel().equals(element))
							DataResource.selectedProject.addArrow(activity, myActivity);
					}
				}
		
		}
		DataResource.saveToDB();
				
	}
	
	private void deleteAction(){
		
		Activities myActivity = DataResource.selectedActivity;
		DataResource.selectedProject.deleteActivity(myActivity);
		
	}
	private void disposeWindow(){
		this.dispose();
	}
	
}