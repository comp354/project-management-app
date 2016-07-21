package graphview_components;

import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import listview_components.Activity_edit;
import listview_components.Activity_form;
import listview_components.Activity_view;
import listview_components.Project_edit;
import listview_components.Project_form;
import listview_components.Project_view;
import resources.UserType;
import saver_loader.DataResource;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar{

	final JMenuBar menuBar = new JMenuBar();
	
	public MenuBar()
	{
		
	    //create menus
	    JMenu projectMenu = new JMenu("Projects");
	    JMenu activityMenu = new JMenu("Activities");
	    
	  if(DataResource.currentUser.getType() == UserType.MANAGER)
	  {
		//create project menu items
		    JMenuItem newProjectMenuItem = new JMenuItem("New");
		    newProjectMenuItem.setActionCommand("New Project");

		    //JMenuItem saveProjectMenuItem = new JMenuItem("Save");
		    //saveProjectMenuItem.setActionCommand("Save");
		    
		    JMenuItem editProjectMenuItem = new JMenuItem("Edit");
		    editProjectMenuItem.setActionCommand("ProjectEdit");
		    
		  //activity menu items
		    JMenuItem newActivityMenuItem = new JMenuItem("New");
		    newActivityMenuItem.setActionCommand("New");
		    
		    JMenuItem editActivityMenuItem = new JMenuItem("Edit");
		    newActivityMenuItem.setActionCommand("ActivityEdit");
		    
		    
		  //project menu items action listeners
		    newProjectMenuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable(){

						@Override
						public void run() {
							
							Project_form frame = new Project_form();
							frame.setVisible(true);
							
						}
	        	});	}});
		    
		    
		    editProjectMenuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				
					if(DataResource.selectedProject != null){
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								Project_edit frame =  new Project_edit();
								frame.setVisible(true);
							}
						});				
					}else{
						JOptionPane.showMessageDialog(new JFrame(),
							    "Must have a selected Project before trying to edit",
							    "No Project Selected",
							    JOptionPane.WARNING_MESSAGE);
						
					}
					
				}
			});
		    
		  //activity menu items action listeners
		    newActivityMenuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
				
					if(DataResource.selectedProject != null){
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								Activity_form frame =  new Activity_form();
								frame.setVisible(true);
							}
						});				
					}else{
						JOptionPane.showMessageDialog(new JFrame(),
							    "Must have a selected Project before trying to add new Activity!",
							    "No Project Selected",
							    JOptionPane.WARNING_MESSAGE);
						
					}
					
				}
			});

		    
		    editActivityMenuItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(DataResource.selectedActivity != null){
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							Activity_edit frame =  new Activity_edit();
							frame.setVisible(true);
						}
					});				
				}else{
					JOptionPane.showMessageDialog(new JFrame(),
						    "Activity must be selected before you can edit",
						    "No Activity Selection",
						    JOptionPane.WARNING_MESSAGE);
				}
				}
			});
		    //add menu items to menus
		    projectMenu.add(newProjectMenuItem);
		    //projectMenu.addSeparator();
		    //projectMenu.add(saveProjectMenuItem);
		    projectMenu.add(editProjectMenuItem);
		    activityMenu.add(newActivityMenuItem);
		    //activityMenu.addSeparator();
		    activityMenu.add(editActivityMenuItem);
	  }
	  
	    JMenuItem exitMenuItem = new JMenuItem("Exit");
	    exitMenuItem.setActionCommand("Exit");
	    
	    JMenuItem viewProjectMenuItem = new JMenuItem("View");
	    viewProjectMenuItem.setActionCommand("ProjectView");
	    
	    JMenuItem viewActivityMenuItem = new JMenuItem("View");
	    viewActivityMenuItem.setActionCommand("View");
	   
	    exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});    
	   
	    viewProjectMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				if(DataResource.selectedProject != null){
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							Project_view frame =  new Project_view();
							frame.setVisible(true);
						}
					});				
				}else{
					JOptionPane.showMessageDialog(new JFrame(),
						    "Must have a selected Project before trying to edit",
						    "No Project Selected",
						    JOptionPane.WARNING_MESSAGE);
					
				}
				
			}
		});
	    
	    viewActivityMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(DataResource.selectedActivity != null){
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						Activity_view frame =  new Activity_view();
						frame.setVisible(true);
					}
				});				
			}else{
				JOptionPane.showMessageDialog(new JFrame(),
					    "Activity must be selected before you can edit",
					    "No Activity Selection",
					    JOptionPane.WARNING_MESSAGE);
			}
			}
		});
	    
	    //projectMenu.addSeparator();
	    projectMenu.add(viewProjectMenuItem);
	    activityMenu.add(viewActivityMenuItem);
	    projectMenu.add(exitMenuItem);
	    
	    //add menu to menu bar
	    menuBar.add(projectMenu);
	    menuBar.add(activityMenu);
	    
	    
	}
	
    public JMenuBar getMenuBar() {
		return menuBar;
	}

}
