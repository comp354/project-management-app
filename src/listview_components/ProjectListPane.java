package listview_components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import resources.Projects;
import saver_loader.DataResource;


@SuppressWarnings("serial")
public class ProjectListPane extends JPanel{

	public static JList<String> list;
	public JScrollPane scrollpane;
	
	public static JLabel title = new JLabel();
	public static DefaultListModel<String> listModel = new DefaultListModel<String>();
	float fontScalar = Toolkit.getDefaultToolkit().getScreenSize().height/1800f;

	public ProjectListPane(){
		
		//Set the Layout Manager of the Panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
	
		//Create an array of existing project Names			
		String[] projectNames = new String[DataResource.projectList.size()];
		for(int i = 0; i < DataResource.projectList.size(); i++)
		projectNames[i] = DataResource.projectList.get(i).getProjectName();
		
		//Add all the names to the list	model
		for(int i = 0; i < DataResource.projectList.size(); i++){
			listModel.addElement(projectNames[i]);
		}
		
		
		//add model to the list
		list = new JList<String>(listModel);
		//Set List variables
		list.setFont(list.getFont().deriveFont(fontScalar*30f));
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    //Add and define Listener for the list
	    list.addListSelectionListener(new ListSelectionListener() {
	        
	    	public void valueChanged(ListSelectionEvent e) {
	         	      
	    		if (e.getValueIsAdjusting()) {//This line prevents double events
	    			DataResource.selectedProject = DataResource.getProjectbyProjectName(list.getSelectedValue());
		        	ActivityListPane.updateTable(DataResource.selectedProject);
	    	    }
	        		
	        }
	      });
					    
		scrollpane = new JScrollPane(list);
		
		c.weightx =1;
		c.weighty = 0.5;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.CENTER;
		
		title.setText("Project List");
		title.setFont(title.getFont().deriveFont(fontScalar*50f));
		
		this.add(title, c);
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 0;
		c.gridy = 1;
		c.weighty = 5;
		this.add(scrollpane, c);
				
		
	}
	
	public static void updateList(){
		
		listModel.clear();
		
		for(Projects element : DataResource.projectList)
			listModel.addElement(element.getProjectName());
		
	}
	
}

