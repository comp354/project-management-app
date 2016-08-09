package listview_components;

import javax.swing.JPanel;

import domain.PertController;
import saver_loader.DataResource;

import java.awt.Canvas;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Pert_view extends JPanel {

	/**
	 * Create the panel.
	 */
	public Pert_view() {
		setLayout(null);
				
		JLabel lblTargetDate = new JLabel("Target Date");
		lblTargetDate.setBounds(59, 275, 68, 14);
		add(lblTargetDate);
		
		JLabel lbl_TD = new JLabel();
		lbl_TD.setBounds(137, 275, 46, 14);
		add(lbl_TD);
		
		JLabel lblStandardDeviation = new JLabel("Standard Deviation");
		lblStandardDeviation.setBounds(266, 275, 92, 14);
		add(lblStandardDeviation);
		
		JLabel lbl_SD = new JLabel(String.valueOf((PertController.getPertData(DataResource.selectedActivity).getStandardDeviation())));
		lbl_SD.setBounds(368, 275, 46, 14);
		add(lbl_SD);
		
		this.paint(this.getGraphics());
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawRect (10, 10, 100, 100);
		g.drawRect (100, 10, 100, 100);
		g.drawRect (10, 100, 100, 100);
		g.drawRect (100, 100, 100, 100);

	}
	
}
