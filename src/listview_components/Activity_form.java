package listview_components;

import javax.swing.*;

import resources.Activities;
import resources.PertData;
import resources.TaskProgress;
import saver_loader.DataResource;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import domain.ActivityController;
import domain.PertController;

@SuppressWarnings("serial")
public class Activity_form extends JFrame {

	private JPanel contentPane;
	private JTextField descriptionField;
	private JTextField startField;
	private JTextField endField;
	private JTextField activityLabelField;
	private JComboBox<String> progressField;
	private JSpinner budgetField;

	private ArrayList<String> dependencies = new ArrayList<String>();
	private ArrayList<String> members = new ArrayList<String>();


	public Activity_form() {

		// Initialize JFrame Settings
		setTitle("ACTIVITY CREATION");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 620, 655);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Create and add Name Field
		activityLabelField = new JTextField();
		activityLabelField.setBounds(216, 23, 124, 20);
		contentPane.add(activityLabelField);
		activityLabelField.setColumns(10);

		// Create and add Description Field
		descriptionField = new JTextField();
		descriptionField.setBounds(216, 120, 124, 20);
		descriptionField.setColumns(10);
		contentPane.add(descriptionField);

		// Create and add Progress Field
		progressField = new JComboBox<String>();
		for(TaskProgress state : TaskProgress.values())
			progressField.addItem(state.name());
		progressField.setBounds(216, 145, 124, 20);
		contentPane.add(progressField);

		SpinnerModel spinModel = new SpinnerNumberModel(0, 0, 9999, 1);  
		budgetField = new JSpinner(spinModel);
		budgetField.setBounds(216, 370, 124, 20);
		contentPane.add(budgetField);

		// Create and add all Labels
		JLabel lblLabel = new JLabel("Name");
		lblLabel.setBounds(21, 26, 160, 14);
		contentPane.add(lblLabel);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(21, 123, 160, 14);
		contentPane.add(lblDescription);

		JLabel lblProgress = new JLabel("Progress");
		lblProgress.setBounds(21, 140, 160, 14);
		contentPane.add(lblProgress);

		JLabel lblBudget = new JLabel("Budget");
		lblBudget.setBounds(21, 370, 160, 14);
		contentPane.add(lblBudget);

		JLabel lblStart = new JLabel("Start Date (DD-MM-YYYY)");
		lblStart.setBounds(21, 64, 170, 14);
		contentPane.add(lblStart);

		JLabel lblEnd = new JLabel("End Date (DD-MM-YYYY)");
		lblEnd.setBounds(21, 94, 170, 14);
		contentPane.add(lblEnd);

		JLabel lblDependencies = new JLabel("Dependencies");
		lblDependencies.setBounds(21, 157, 160, 14);
		contentPane.add(lblDependencies);

		JLabel lblResources = new JLabel("Resources");
		lblResources.setBounds(21, 267, 160, 14);
		contentPane.add(lblResources);

		// Create and add all text Fields
		startField = new JTextField();
		startField.setBounds(216, 61, 124, 20);
		contentPane.add(startField);
		startField.setColumns(10);

		// Create and add all text Fields
		endField = new JTextField();
		endField.setBounds(216, 91, 124, 20);
		contentPane.add(endField);
		endField.setColumns(10);

		// Create an array of the current Activities
		int activityCount = DataResource.selectedProject.getActivityList().size();
		Activities[] activityList = new Activities[activityCount];
		DataResource.selectedProject.getActivityList().toArray(activityList);

		// Create Selections from the list of Activities and their labels
		String[] selections = new String[activityCount];

		for (int i = 0; i < activityCount; i++)
			selections[i] = activityList[i].getLabel();

		// Create list with selections
		final JList<String> selectionList = new JList<String>(selections);

		// Create ScrollPane with list inside and add to Frame
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_1.setBounds(216, 175, 101, 88);
		scrollPane_1.setViewportView(selectionList);

		contentPane.add(scrollPane_1);

		String[] memberNames = new String[DataResource.projectMembers.size()];

		for (int i = 0; i < DataResource.projectMembers.size(); i++) {
			memberNames[i] = DataResource.projectMembers.get(i).getName();
		}
		final JList<String> memberList = new JList<String>(memberNames);
		// Create ScrollPane with list inside and add to Frame
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane_2.setBounds(216, 267, 101, 88);
		scrollPane_2.setViewportView(memberList);

		contentPane.add(scrollPane_2);

		// Initialize and set Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(28, 400, 89, 23);
		contentPane.add(btnCancel);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(138, 400, 89, 23);
		contentPane.add(btnSave);

		JButton btnPert = new JButton("Add Pert Info");
		btnPert.setBounds(229, 400, 89, 23);
		contentPane.add(btnPert);


		// Add and define ActionListeners to the buttons

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				disposeWindow();
			}
		});


		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try
				{
					DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
					Date start = dateFormatter.parse(startField.getText());
					Date end = dateFormatter.parse(endField.getText());

					if (start.before(end) && !descriptionField.getText().isEmpty() && !startField.getText().isEmpty() && !endField.getText().isEmpty() && !activityLabelField.getText().isEmpty())
					{

						int n = JOptionPane.showConfirmDialog(new JFrame(),
								"Would you Like to add PERT info",
								"Pert Info",
								JOptionPane.YES_NO_OPTION);
						boolean specifyPERT = false;

						if(n>0){specifyPERT = true;	}

						ActivityController.addActivity(descriptionField.getText(), startField.getText(), endField.getText(), activityLabelField.getText(), dependencies, members, progressField.getSelectedItem().toString(), (int)budgetField.getModel().getValue(),specifyPERT);

						if(n>0){

							SwingUtilities.invokeLater(new Runnable(){

								@Override
								public void run() {

									Pert_form frame = new Pert_form();
									frame.setVisible(true);
								};
							});
						}
						disposeWindow();
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

				}
				catch (Exception exception)
				{
					JOptionPane.showMessageDialog(new JFrame(),
							"Please Fill in all values correctly",
							"Values are incorrect format or missing",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

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
	}
	private void disposeWindow() {
		this.dispose();
	}
}