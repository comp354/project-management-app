package graphview_components;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import driver.ClientLauncher;
import resources.UserType;
import resources.Users;
import saver_loader.DataResource;

import java.sql.*;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel{

	
	JLabel username = new JLabel("Enter username");
	JLabel password = new JLabel("Enter password");
	JLabel authorizationStatus = new JLabel("authorization pending...");
	JLabel title = new JLabel("Ultimate Sandwich Program Management System", JLabel.CENTER);
	
	JTextField usernameField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton =  new JButton("login");
	
	float fontScalar = Toolkit.getDefaultToolkit().getScreenSize().height/1800f;
	

	
	public LoginPanel(){
		
		Font fs40 = username.getFont().deriveFont(fontScalar*35f);
		Font fs45 = username.getFont().deriveFont(fontScalar*40f);


		//Set font size and color for each message
		title.setFont(fs45);
		title.setForeground(Color.decode("#d9d9d9"));
		username.setFont(fs40);	
		username.setForeground(Color.decode("#d9d9d9"));
		password.setFont(fs40);
		password.setForeground(Color.decode("#d9d9d9"));
		usernameField.setFont(fs40);
		passwordField.setFont(fs40);
		loginButton.setFont(fs40);
		authorizationStatus.setFont(fs40);
		authorizationStatus.setForeground(Color.decode("#d9d9d9"));


		//Set Layout and position the elements
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 7;
		c.weightx = 4;
		
		JPanel transparent = new JPanel();
		transparent.setOpaque(false);
		this.add(transparent, c);
		
		c = new GridBagConstraints();
		
		c.weighty = 1;
		c.weightx = 1;
		c.gridheight=1;
		c.gridx = 1;
		c.gridy = 0;
		this.add(title, c);
		
		c = new GridBagConstraints();
		
		c.weighty=0.25;
		c.gridy = 1;
		this.add(username, c);
		
		c = new GridBagConstraints();
		
		c.gridy = 2;
		usernameField.setColumns(15);
		this.add(usernameField, c);
		
		c = new GridBagConstraints();
		
		c.gridy = 3;		
		this.add(password, c);
		
		c = new GridBagConstraints();
		
		c.gridy = 4;
		passwordField.setColumns(15);
		this.add(passwordField, c);
		
		c = new GridBagConstraints();
        passwordField.addActionListener(new EnterListener());
		
		c.gridy = 5;
		loginButton.addActionListener(new ButtonListener());
		this.add(loginButton, c);
		
		c = new GridBagConstraints();
		
		c.weighty = 1;
		c.gridy = 6;
		this.add(authorizationStatus, c);
		
	}
	
		
	@Override
	public void paintComponent(Graphics g){
		
		BufferedImage sandwhich = null;
		
		try {
			sandwhich = ImageIO.read(new File("ultimateSandwhich.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		BufferedImage wood = null;
		
		try {
			wood = ImageIO.read(new File("5wood.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	
		g.drawImage(sandwhich, 0, 0, getWidth()/3, getHeight(), null);
		g.drawImage(wood, getWidth()/3, 0, getWidth(), getHeight(), null);

		
	}

	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			DataResource.setDatabase("jdbc:sqlite:ultimate_sandwich.db");
			Connection connection = DataResource.createConnectionToDB(DataResource.dataBase);
	        PreparedStatement ps;
	        
	        try{
	        	
	        	ps = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
	        	
	        	ps.setString(1, usernameField.getText());
	            ps.setString(2, String.valueOf(passwordField.getPassword()));
	            ResultSet result = ps.executeQuery();
	            if(result.next()){
	            	
	            	authorizationStatus.setText("Login Successful");
	    			authorizationStatus.paintImmediately(authorizationStatus.getVisibleRect());
	    			
	    			// Create Current User 
	    			DataResource.currentUser = new Users(result.getString(4),result.getString(2),result.getString(3),
	    					result.getString(5),result.getInt(1),result.getString(6));
	    			
	    			if(DataResource.currentUser.getType() == UserType.MANAGER) {
	    				DataResource.loadManagerDataFromDB();
	    			} else {
	    				DataResource.loadMemberDataFromDB();
	    			}
	    			
	    			try {
	    			    Thread.sleep(1000);                 //1000 milliseconds is one second.
	    			} catch(InterruptedException ex) {
	    			    Thread.currentThread().interrupt();
	    			}
	    			
	    			ClientLauncher.loginFrame.dispose();
	    			
	    			SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							ClientLauncher.launchCLient();
						}
					});

	    			
	            }
	            else
	            {
	            	authorizationStatus.setText("login failed: invalid username or password");
	            	authorizationStatus.paintImmediately(authorizationStatus.getVisibleRect());	
	            }

	        }catch(Exception exception) {
	        	System.out.println(exception.getMessage());
	        }
	        
	        try{
	        	connection.close();
	        }catch(Exception closingException)
	        {
	        	System.out.println(closingException.getMessage());
	        }
		}	
	}

    private class EnterListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            DataResource.setDatabase("jdbc:sqlite:ultimate_sandwich.db");
            Connection connection = DataResource.createConnectionToDB(DataResource.dataBase);
            PreparedStatement ps;

            try{

                ps = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");

                ps.setString(1, usernameField.getText());
                ps.setString(2, String.valueOf(passwordField.getPassword()));
                ResultSet result = ps.executeQuery();
                if(result.next()){

                    authorizationStatus.setText("login successful");
                    authorizationStatus.paintImmediately(authorizationStatus.getVisibleRect());

                    // Create Current User
                    DataResource.currentUser = new Users(result.getString(4),result.getString(2),result.getString(3),
                            result.getString(5),result.getInt(1),result.getString(6));

                    if(DataResource.currentUser.getType() == UserType.MANAGER) {
                        DataResource.loadManagerDataFromDB();
                    } else {
                        DataResource.loadMemberDataFromDB();
                    }

                    try {
                        Thread.sleep(1000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    ClientLauncher.loginFrame.dispose();

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ClientLauncher.launchCLient();
                        }
                    });


                }
                else
                {
                    authorizationStatus.setText("login failed: invalid username or password");
                    authorizationStatus.paintImmediately(authorizationStatus.getVisibleRect());
                }

            }catch(Exception exception) {
                System.out.println(exception.getMessage());
            }

            try{
                connection.close();
            }catch(Exception closingException)
            {
                System.out.println(closingException.getMessage());
            }
        }
    }

}
