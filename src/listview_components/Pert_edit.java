package listview_components;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jfree.util.StringUtils;

import domain.PertController;
import resources.Activities;
import resources.PertData;
import saver_loader.DataResource;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;


public class Pert_edit extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField txt_P;
	private JTextField txt_O;
	private JTextField txt_M;
	private JTextField txt_SD;
	
	public Pert_edit(Activities activity) {
		
		this.setSize(359, 214);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{100, 100, 100, 100, 0};
		gridBagLayout.rowHeights = new int[]{65, 65, 65, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblOptimisticTime = new JLabel("Optimistic Time");
		GridBagConstraints gbc_lblOptimisticTime = new GridBagConstraints();
		gbc_lblOptimisticTime.fill = GridBagConstraints.VERTICAL;
		gbc_lblOptimisticTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblOptimisticTime.gridx = 0;
		gbc_lblOptimisticTime.gridy = 0;
		getContentPane().add(lblOptimisticTime, gbc_lblOptimisticTime);
		
		txt_O = new JTextField();
		txt_O.setColumns(10);
		GridBagConstraints gbc_txt_O = new GridBagConstraints();
		gbc_txt_O.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_O.insets = new Insets(0, 0, 5, 5);
		gbc_txt_O.gridx = 1;
		gbc_txt_O.gridy = 0;
		getContentPane().add(txt_O, gbc_txt_O);
		
		JLabel lblPestimisticTime = new JLabel("Pestimistic Time");
		GridBagConstraints gbc_lblPestimisticTime = new GridBagConstraints();
		gbc_lblPestimisticTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblPestimisticTime.gridx = 2;
		gbc_lblPestimisticTime.gridy = 0;
		getContentPane().add(lblPestimisticTime, gbc_lblPestimisticTime);
		
		txt_P = new JTextField();
		txt_P.setColumns(10);
		GridBagConstraints gbc_txt_P = new GridBagConstraints();
		gbc_txt_P.insets = new Insets(0, 0, 5, 0);
		gbc_txt_P.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_P.gridx = 3;
		gbc_txt_P.gridy = 0;
		getContentPane().add(txt_P, gbc_txt_P);
		
		JLabel lblMostLikelyTime = new JLabel("Most Likely Time");
		GridBagConstraints gbc_lblMostLikelyTime = new GridBagConstraints();
		gbc_lblMostLikelyTime.fill = GridBagConstraints.VERTICAL;
		gbc_lblMostLikelyTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblMostLikelyTime.gridx = 0;
		gbc_lblMostLikelyTime.gridy = 1;
		getContentPane().add(lblMostLikelyTime, gbc_lblMostLikelyTime);
		
		txt_M = new JTextField();
		txt_M.setColumns(10);
		GridBagConstraints gbc_txt_M = new GridBagConstraints();
		gbc_txt_M.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_M.insets = new Insets(0, 0, 5, 5);
		gbc_txt_M.gridx = 1;
		gbc_txt_M.gridy = 1;
		getContentPane().add(txt_M, gbc_txt_M);
		
		JLabel label = new JLabel("Standard Deviation");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 2;
		gbc_label.gridy = 1;
		getContentPane().add(label, gbc_label);
		
		txt_SD = new JTextField();
		txt_SD.setColumns(10);
		GridBagConstraints gbc_txt_SD = new GridBagConstraints();
		gbc_txt_SD.insets = new Insets(0, 0, 5, 0);
		gbc_txt_SD.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_SD.gridx = 3;
		gbc_txt_SD.gridy = 1;
		getContentPane().add(txt_SD, gbc_txt_SD);
		
		
		PertData pert = PertController.getPertData(activity);
		txt_P.setText(String.valueOf(pert.getPestimisticTime()));
		txt_O.setText(String.valueOf(pert.getOptimisticTime()));
		txt_M.setText(String.valueOf(pert.getMostLikelyTime()));
		txt_SD.setText(String.valueOf(pert.getStandardDeviation()));
		
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				disposeWindow();
				
			}
		});
		GridBagConstraints gbc_cancel = new GridBagConstraints();
		gbc_cancel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cancel.insets = new Insets(0, 0, 0, 5);
		gbc_cancel.gridx = 1;
		gbc_cancel.gridy = 2;
		getContentPane().add(cancel, gbc_cancel);
		
		JButton Save = new JButton("Save");
		Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
							
				try{
				PertController.addPertData(new PertData((Activities.getActivityCount()),Integer.valueOf(txt_P.getText()),Integer.valueOf(txt_O.getText()), Integer.valueOf(txt_M.getText()), Integer.valueOf(txt_SD.getText())));
				disposeWindow();
				 				
				}catch(NumberFormatException f){
					
					JOptionPane.showMessageDialog(new Frame(),
						    "Please Input Numbers",
						    "Error",
						    JOptionPane.WARNING_MESSAGE);	
				}
			}
		});
		GridBagConstraints gbc_Save = new GridBagConstraints();
		gbc_Save.gridx = 3;
		gbc_Save.gridy = 2;
		getContentPane().add(Save, gbc_Save);
		
	}
	private void disposeWindow() {
		this.dispose();
		
	}
	
	
}
