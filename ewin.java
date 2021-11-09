package test3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ewin {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void launch(String e) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ewin window = new ewin(e);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ewin(String e) {
		initialize(e);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String e) {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 573, 161);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel eMsgLabel = new JLabel("<html>"+e+"<html>");
		eMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);
		eMsgLabel.setBounds(12, 10, 533, 102);
		frame.getContentPane().add(eMsgLabel);
		
		frame.setLocationRelativeTo(null);
	}
}
