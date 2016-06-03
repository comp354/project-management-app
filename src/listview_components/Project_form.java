package listview_components;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import driver.ClientLauncher;
import resources.Projects;
import resources.Users;
import saver_loader.DataResource;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;

public class Project_form extends JFrame {

	private JPanel contentPane;
	private JTextField NameField;
	private JTextField DescField;
	private JTextField BudgetField;

	

	/**
	 * Create the frame.
	 */
	public Project_form() {
		
		setTitle("PROJECT CREATION");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 266, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel NameLabel = new JLabel("Name");
		NameLabel.setForeground(Color.BLACK);
		NameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		NameLabel.setBounds(10, 11, 55, 36);
		contentPane.add(NameLabel);
		
		JLabel DescLabel = new JLabel("Description");
		DescLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		DescLabel.setBounds(10, 76, 83, 26);
		contentPane.add(DescLabel);
		
		JLabel Budget = new JLabel("Budget");
		Budget.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Budget.setBounds(10, 131, 59, 26);
		contentPane.add(Budget);
		
		NameField = new JTextField();
		NameField.setBounds(91, 21, 147, 20);
		contentPane.add(NameField);
		NameField.setColumns(10);
		
		DescField = new JTextField();
		DescField.setBounds(91, 81, 147, 20);
		contentPane.add(DescField);
		DescField.setColumns(10);
		
		BudgetField = new JTextField();
		BudgetField.setBounds(91, 136, 147, 20);
		contentPane.add(BudgetField);
		BudgetField.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		EndingListener buttonEar = new EndingListener();
		btnCancel.addActionListener(buttonEar);
		btnCancel.setBounds(34, 183, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		ButtonListener buttonSave = new ButtonListener();
		btnSave.setBounds(130, 183, 89, 23);
		btnSave.addActionListener(new ButtonListener());
		
		contentPane.add(btnSave);
		
		
	}
	
	private void SaveAction () {
        ArrayList<Users> userList = new ArrayList<Users>();
        userList.add(DataResource.currentUser);
        Projects newProject = new Projects(NameField.getText(), userList, "",1,
        		DescField.getText(), Double.parseDouble(BudgetField.getText()));
        DataResource.projectList.add(newProject);
        
        String name = NameField.getText();
       
        ProjectListPane.updateList();
        this.dispose();
        
        
    }
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			SaveAction();
			
			
			//if(savebutton.getParent().getClass() == new Project_form().getClass())
				//System.out.print(true);
			
			
			
			
		}
	}
	
	
}
