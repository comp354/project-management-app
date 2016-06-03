package graphview_components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public  class TabPanel extends JPanel {
	
	
	public JTabbedPane tabPane;
	float fontScalar = Toolkit.getDefaultToolkit().getScreenSize().height/1800f;

	public TabPanel(){
		
		tabPane = new JTabbedPane();
	    

		Font fs30 = tabPane.getFont().deriveFont(Font.BOLD, fontScalar*30f);
		
		tabPane.setFont(fs30);
	   
	    //This line insures the tab panel doesnt grow as tabs get added
	    tabPane.setPreferredSize(this.getPreferredSize());
	   
	  //GridLayout so the child will fill the whole container
	  	this.setLayout(new GridLayout());
	    
	    this.add(tabPane);

	}

	@Override
	public void paintComponent(Graphics g){
		
		BufferedImage blurBackground = null;
		
		try {
			blurBackground = ImageIO.read(new File("blur.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		g.drawImage(blurBackground, 0, 0, getWidth(), getHeight(), null);
		
		g.setColor(Color.black);
		g.setFont(g.getFont().deriveFont(Font.BOLD, fontScalar*50f));
		g.drawString("No Project Currently in Progress", getWidth()/4, getHeight()/2);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, fontScalar*40f));
		g.drawString("Create a new Project to get Started", getWidth()/4, (int) (getHeight()/1.6));

		

		
	}
	
	public void setTabPanel(JTabbedPane newTabPane){
		this.tabPane = newTabPane;
	}
	
	public JTabbedPane getTabPanel(){
		return this.tabPane;
	}
	
	
	
	public  void  addProjectTab(String projectName, int projectId){
		
		
		
		//Create a Draw Panel
		DrawPanel drawTab = new DrawPanel();
		
		drawTab.setPreferredSize(this.getPreferredSize());		
		
		tabPane.insertTab(projectName, null, drawTab, null, projectId);
		drawTab.drawArrows();
		
	}
	

}
