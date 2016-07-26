package listview_components;

import javax.swing.JFrame;
import javax.swing.JPanel;

import resources.UserType;
import saver_loader.DataResource;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.awt.Color;
import javax.swing.border.BevelBorder;

import org.jfree.ui.RefineryUtilities;

import graphview_components.GanttChart;

import javax.swing.JButton;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Project_view extends JFrame {

	private JPanel contentPane;
	private JLabel nameField;
	private JLabel descriptionField;
	private JLabel BudgetField;


	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public Project_view() {

		// Initialize Frame Settings
		setTitle("PROJECT VIEW");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 349, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Initialize and create Labels
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

		// Initialize and create Text Fields
		nameField = new JLabel(DataResource.selectedProject.getProjectName());
		nameField.setBounds(139, 67, 147, 20);
		contentPane.add(nameField);

		descriptionField = new JLabel(DataResource.selectedProject.getDescription());
		descriptionField.setBounds(139, 123, 200, 20);
		contentPane.add(descriptionField);

		BudgetField = new JLabel(Double.toString(DataResource.selectedProject.getBudget()));
		BudgetField.setBounds(139, 179, 147, 20);
		contentPane.add(BudgetField);

		// Initialize and cancel Buttons
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(197, 261, 89, 23);
		contentPane.add(btnCancel);

        if (DataResource.currentUser.getType() == UserType.MANAGER)
        {
            JButton btnGantt = new JButton("Generate Gantt");
            btnGantt.setBounds(46, 261, 120, 23);
            contentPane.add(btnGantt);

            btnGantt.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ganttAction();
                    disposeWindow();
                }
            });
        }

		JLabel editLabel = new JLabel("View");
		Font font = editLabel.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		editLabel.setFont(font.deriveFont(attributes));
		// editLabel.setFont(editLabel.getFont().deriveFont(30f));

		editLabel.setBounds(23, 11, 46, 14);
		contentPane.add(editLabel);

		// Set and define listeners for both buttons

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				disposeWindow();
			}
		});
	}
	
	private void ganttAction() {
		final GanttChart chart = new GanttChart(DataResource.selectedProject.getProjectName());
		chart.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
	
	private void disposeWindow() {
		this.dispose();
	}
}