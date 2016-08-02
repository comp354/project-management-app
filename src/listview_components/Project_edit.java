package listview_components;

import javax.swing.JFrame;
import javax.swing.JPanel;
import saver_loader.DataResource;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import domain.ProjectController;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Project_edit extends JFrame {

	private JPanel contentPane;
	private JTextField nameField;
	private JTextField descriptionField;
	private JTextField BudgetField;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public Project_edit() {
		
		//Initialize Frame Settings
		setTitle("PROJECT EDITING");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 349, 453);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Initialize and create Labels
		JLabel NameLabel = new JLabel("Name");
		NameLabel.setForeground(Color.BLACK);
		NameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		NameLabel.setBounds(23, 57, 55, 36);
		contentPane.add(NameLabel);
		
		JLabel DescLabel = new JLabel("Description");
		DescLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DescLabel.setBounds(23, 118, 83, 26);
		contentPane.add(DescLabel);
		
		JLabel Budget = new JLabel("Budget");
		Budget.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Budget.setBounds(23, 174, 59, 26);
		contentPane.add(Budget);
		
		//Initialize and create Text Fields
		nameField = new JTextField(DataResource.selectedProject.getProjectName());
		nameField.setBounds(139, 67, 147, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		descriptionField = new JTextField(DataResource.selectedProject.getDescription());
		descriptionField.setBounds(139, 123, 147, 20);
		contentPane.add(descriptionField);
		descriptionField.setColumns(10);
		
		BudgetField = new JTextField(Double.toString(DataResource.selectedProject.getBudget()));
		BudgetField.setBounds(139, 179, 147, 20);
		contentPane.add(BudgetField);
		BudgetField.setColumns(10);
		
		//Initialize and create Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(197, 261, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(46, 261, 89, 23);
		contentPane.add(btnSave);
		
		JLabel editLabel = new JLabel("Edit");
		Font font = editLabel.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		editLabel.setFont(font.deriveFont(attributes));
		//editLabel.setFont(editLabel.getFont().deriveFont(30f));
		
		editLabel.setBounds(23, 11, 46, 14);
		contentPane.add(editLabel);
		
		JLabel lblDelete = new JLabel("Delete");
		lblDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDelete.setBounds(23, 309, 46, 14);
		contentPane.add(lblDelete);
		
		JLabel lblDoYouWant = new JLabel("Do you want to delete?");
		lblDoYouWant.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDoYouWant.setBounds(23, 345, 157, 14);
		contentPane.add(lblDoYouWant);
		
		JButton btnNewButton = new JButton("Delete");
		btnNewButton.setBounds(197, 342, 89, 23);
		contentPane.add(btnNewButton);
		
		//Set and define listeners for both buttons
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				disposeWindow();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
				
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteAction();
				disposeWindow();
			}
		});
		
		
	}
		
	private void saveAction () {

		try
		{
			double budget = Double.parseDouble(BudgetField.getText());
			if (!nameField.getText().isEmpty() && !descriptionField.getText().isEmpty() && budget > 0)
			{
				ProjectController.editProject(nameField.getText(), descriptionField.getText(), Double.parseDouble(BudgetField.getText()));	
				disposeWindow();
			}
			else
			{
				JOptionPane.showMessageDialog(new JFrame(),
	                    "Please Fill in all values correctly",
	                    "Values are incorrect format or missing values",
	                    JOptionPane.WARNING_MESSAGE);
			}
		}
		catch (Exception exception)
		{
			JOptionPane.showMessageDialog(new JFrame(),
                    "Budget must be numeric",
                    "Budget must be numeric",
                    JOptionPane.WARNING_MESSAGE);
		}
    }
	
	private void disposeWindow(){
		this.dispose();
	}
	
	private void deleteAction () {
		ProjectController.deleteProject();
	}
}