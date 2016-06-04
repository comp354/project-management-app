package listview_components;

import javax.swing.JFrame;
import javax.swing.JPanel;
import resources.Projects;
import resources.Users;
import saver_loader.DataResource;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Project_form extends JFrame {

	private JPanel contentPane;
	private JTextField NameField;
	private JTextField DescField;
	private JTextField BudgetField;

	

	/**
	 * Create the frame.
	 */
	public Project_form() {
		
		//Initialize Frame Settings
		setTitle("PROJECT CREATION");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 266, 255);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Initialize and create Labels
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
		
		//Initialize and create Text Fields
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
		
		//Initialize and create Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(34, 183, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(130, 183, 89, 23);
		contentPane.add(btnSave);
		
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
		
	}
		
	private void saveAction () {
		
		//Initialize A user list and add current user to it
		ArrayList<Users> userList = new ArrayList<Users>();
        userList.add(DataResource.currentUser);
        
        // Create the date
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        String date = df.format(dateobj);
        
        
        //Create a new Project with information from the form, userList, and date
        Projects newProject = new Projects(NameField.getText(), userList, date,DataResource.currentUser.getID(),
        		DescField.getText(), Double.parseDouble(BudgetField.getText()));
        DataResource.projectList.add(newProject);
        
        ProjectListPane.updateList();
        this.dispose();
                
    }
	
	private void disposeWindow(){
		this.dispose();
	}
	
	
}

