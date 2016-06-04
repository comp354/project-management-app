package listview_components;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.DefaultEdge;

import resources.Activities;
import resources.Projects;
import saver_loader.DataResource;

@SuppressWarnings("serial")
public class ActivityListPane extends JPanel implements MouseListener {

	public static JTable table = new JTable();
	private  DefaultTableModel mod = new DefaultTableModel();
	
	float fontScalar = Toolkit.getDefaultToolkit().getScreenSize().height/1800f;
	JLabel title;
		
	public ActivityListPane(){
		
		super();	
		
		//LayoutManager
		this.setLayout(new BorderLayout());
		
		//Set Headers to the Table Model
		String[] columnHeaders = {"Label" , "Description", "Duration", "Depedencies"};
		mod.setColumnIdentifiers(columnHeaders);
		
		
		
		//Set Basic Table Options
		table.setModel(mod);
		table.setFillsViewportHeight(true);
		table.setFont(table.getFont().deriveFont(fontScalar*30f));
		table.setRowHeight(35);
		table.getTableHeader().setFont(table.getFont().deriveFont(40f));
		table.addMouseListener(this);
		table.setRowSelectionAllowed(true);
		table.setEnabled(true);

	
		//add the table to a ScrollPanel
		JScrollPane scrollpane = new JScrollPane(table);
		
		//Set the Title
		title = new JLabel("               Activity ViewPort");
		title.setFont(title.getFont().deriveFont(fontScalar*50f));
		
		//Add title and Table(scrollpane) to Panel
		this.add(title, BorderLayout.PAGE_START);
		this.add(scrollpane);
		
	}
	
	//Method deletes everything from table and makes new rows with the data from selected Project
	public static void updateTable(Projects selectedProject){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rows = model.getRowCount();
				
		//Remove Current Previous Rows from the table
		for (int i = rows - 1; i >= 0; i--) 
		    model.removeRow(i);
		
		//Create an Array of the activities we need to add from the Selected Project
		int activityCount = selectedProject.getActivityList().size();
		Activities[] activityArray = new Activities [activityCount];
		selectedProject.getActivityList().toArray(activityArray);
		
		//Add new Rows
		for(int i = 0; i < activityCount; i++){
			
			//-----------The first task is to create a list of dependencies-------
			Set<DefaultEdge> edgeList=  selectedProject.getIncomingArrowsOfActivity(activityArray[i]);
			String dependencies = "";
			
			if(edgeList.isEmpty())
			 dependencies = "none";
			else
				dependencies = "| ";
			
			for(DefaultEdge e: edgeList)
				dependencies += selectedProject.getActivityBefore(e).getLabel()+ " | ";
			
			//Add The rows		
			Object data[] = {activityArray[i].getLabel(), activityArray[i].getDescription(),
								activityArray[i].getDuration(), dependencies}; 
			
			model.addRow(data);	
			
		}
				
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.rowAtPoint(e.getPoint());
		
		if(row >= 0){
		
			String activityLabel = (String) table.getValueAt(row, 0);
			DataResource.selectedActivity= DataResource.selectedProject.getActivityByLabel(activityLabel);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}

