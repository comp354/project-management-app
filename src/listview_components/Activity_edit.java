package listview_components;

import javax.swing.*;

import resources.Activities;
import resources.Users;
import saver_loader.DataResource;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class Activity_edit extends JFrame {

	private JPanel contentPane;
	private JTextField descriptionField;
	private JTextField startField;
	private JTextField endField;
	private JTextField activityLabelField;

	private ArrayList<String> dependencies = new ArrayList<String>();
	private ArrayList<String> members = new ArrayList<String>();

	public Activity_edit() {

		// Initialize JFrame Settings
		setTitle("EDITING");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 426, 562);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Create and add Description Field
		descriptionField = new JTextField(DataResource.selectedActivity.getDescription());
		descriptionField.setBounds(226, 23, 124, 20);
		descriptionField.setColumns(10);
		contentPane.add(descriptionField);

		// Create and add all Labels
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(64, 26, 124, 14);
		contentPane.add(lblDescription);

		JLabel lblStart = new JLabel("Start Date (DD-MM-YYYY)");
		lblStart.setBounds(64, 64, 124, 14);
		contentPane.add(lblStart);

		JLabel lblEnd = new JLabel("End Date (DD-MM-YYYY)");
		lblEnd.setBounds(64, 94, 124, 14);
		contentPane.add(lblEnd);

		JLabel lblLabel = new JLabel("Label");
		lblLabel.setBounds(64, 123, 46, 14);
		contentPane.add(lblLabel);

		JLabel lblDependencies = new JLabel("Dependencies");
		lblDependencies.setBounds(64, 193, 80, 14);
		contentPane.add(lblDependencies);

		JLabel lblResources = new JLabel("Resources");
		lblResources.setBounds(64, 293, 80, 14);
		contentPane.add(lblResources);

		// Create and add all text Fields
		String initialStart = DataResource.dateFormatter.format(DataResource.selectedActivity.getStartDate());
		startField = new JTextField(initialStart.toString());
		startField.setBounds(226, 61, 124, 20);
		contentPane.add(startField);
		startField.setColumns(10);

		String initialEnd = DataResource.dateFormatter.format(DataResource.selectedActivity.getEndDate());
		endField = new JTextField(initialEnd.toString());
		endField.setBounds(226, 91, 124, 20);
		contentPane.add(endField);
		endField.setColumns(10);

		activityLabelField = new JTextField(DataResource.selectedActivity.getLabel());
		activityLabelField.setBounds(226, 120, 58, 20);
		contentPane.add(activityLabelField);
		activityLabelField.setColumns(10);

		// Create an array of the current Activities
		int activityCount = DataResource.selectedProject.getActivityList().size();
		Activities[] activityList = new Activities[activityCount];
		DataResource.selectedProject.getActivityList().toArray(activityList);

		// Create Selections from the list of Activities and their labels
		String[] selections = new String[activityCount];

		// Get the currently selected dependencies for this activity
		ArrayList<Activities> currentSelections = DataResource.selectedProject
				.getSetofDependencyActivities(DataResource.selectedActivity);

		for (int i = 0; i < activityCount; i++) {
			if (!DataResource.selectedActivity.getLabel().equals(activityList[i].getLabel()))
				selections[i] = activityList[i].getLabel();
		}

		// Create ScrollPane with list inside and add to Frame
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(226, 175, 101, 88);

		contentPane.add(scrollPane_1);

		// Create list with selections
		final JList<String> selectionList = new JList<String>(selections);
		selectionList.setBounds(232, 192, 95, 82);

		// Set the default selections to current dependent activities
		int[] selectedIndices = new int[currentSelections.size()];
		for (int i = 0; i < currentSelections.size(); i++) {
			for (int j = 0; j < activityList.length; j++) {
				if (currentSelections.get(i).getId() == activityList[j].getId())
					selectedIndices[i] = j;
			}
		}
		selectionList.setSelectedIndices(selectedIndices);
		contentPane.add(selectionList);

		// Add to viewport
		scrollPane_1.setViewportView(selectionList);

		ArrayList<Users> currentMembers = DataResource.selectedActivity.getMemberList();
		String[] memberNames = new String[DataResource.projectMembers.size()];

		for (int i = 0; i < DataResource.projectMembers.size(); i++) {
			memberNames[i] = DataResource.projectMembers.get(i).getName();
		}

		// Create ScrollPane with list inside and add to Frame
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_2.setBounds(226, 275, 101, 88);

		contentPane.add(scrollPane_2);

		final JList<String> memberList = new JList<String>(memberNames);
		memberList.setBounds(232, 192, 95, 82);

		int[] memberIndices = new int[currentMembers.size()];
		for (int i = 0; i < currentMembers.size(); i++) {
			for (int j = 0; j < DataResource.projectMembers.size(); j++) {
				if (currentMembers.get(i).getID() == DataResource.projectMembers.get(j).getID())
					memberIndices[i] = j;
			}
		}

		memberList.setSelectedIndices(memberIndices);
		contentPane.add(memberList);

		scrollPane_2.setViewportView(memberList);

		// Initialize and set Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(64, 419, 89, 23);
		contentPane.add(btnCancel);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(238, 419, 89, 23);
		contentPane.add(btnSave);

		JLabel lblDoYouWant = new JLabel("Do you want to delete?");
		lblDoYouWant.setBounds(64, 471, 124, 23);
		contentPane.add(lblDoYouWant);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(238, 471, 89, 23);
		contentPane.add(btnDelete);

		// Create the listListener for dependency choices
		selectionList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (e.getValueIsAdjusting()) {// This line prevents double
												// events

					dependencies = (ArrayList<String>) selectionList.getSelectedValuesList();

				}
			}
		});

		// Create the listListener for member choices
		memberList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {// This line prevents double
												// events
					members = (ArrayList<String>) memberList.getSelectedValuesList();
				}
			}
		});

		// Add and define ActionListeners to the buttons

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

	private void saveAction() {
		try {
			DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
			Date start = dateFormatter.parse(startField.getText());
			Date end = dateFormatter.parse(endField.getText());
			
			Activities myActivity = DataResource.selectedActivity;

            if (start.before(end) && !descriptionField.getText().isEmpty() && !activityLabelField.getText().isEmpty())
            {
                myActivity.setDescription(descriptionField.getText());
                myActivity.setStartDate(start);
                myActivity.setEndDate(end);
                myActivity.setLabel(activityLabelField.getText());

                if (!dependencies.isEmpty()) {

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
                //DataResource.saveToDB();
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
                    "End date must be AFTER start date",
                    "Incorrect Dates",
                    JOptionPane.WARNING_MESSAGE);
		}

	}

	private void deleteAction() {

		Activities myActivity = DataResource.selectedActivity;
		DataResource.selectedProject.deleteActivity(myActivity);

	}

	private void disposeWindow() {
		this.dispose();
	}

}