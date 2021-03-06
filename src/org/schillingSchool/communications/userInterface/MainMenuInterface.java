package org.schillingSchool.communications.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.schillingSchool.communications.utils.Utils;

public class MainMenuInterface extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private static String TITLE = "Schilling Comm Network";
	final private static String CLIENT_BUTTON = "Start Client";
	final private static String SERVER_BUTTON = "Start Server";
	
	private GridBagConstraints constraints;
	
	public MainMenuInterface() {
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		setOpeningLayout();
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(1250, 750));
		setSize(getPreferredSize());
		getContentPane().setBackground(Color.BLACK);
		setVisible(true);
		Utils.getLogger().info("Main Menu Opened");
	}

	private void setOpeningLayout() {

		constraints.ipadx = 50;
		constraints.ipady = 15;
		
		//load button that starts client
		JButton startClient = new JButton(CLIENT_BUTTON);
		startClient.addActionListener(this);
		startClient.setActionCommand(CLIENT_BUTTON);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0; //"x" coordinate is 0
		constraints.gridy = 0; //"y" coordinate is 0
		add(startClient, constraints);
		
		//load "or" in between two buttons
		JLabel orLabel = new JLabel("or");
		orLabel.setHorizontalAlignment(SwingConstants.CENTER);
		orLabel.setVerticalAlignment(SwingConstants.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0; //"x" coordinate is 0
		constraints.gridy = 1; //"y" coordinate is 1
		add(orLabel, constraints);
		
		//load button that starts server
		JButton startServer = new JButton(SERVER_BUTTON);
		startServer.addActionListener(this);
		startServer.setActionCommand(SERVER_BUTTON);
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0; //"x" coordinate is 0
		constraints.gridy = 2; //"y" coordinate is 2
		add(startServer, constraints);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == CLIENT_BUTTON)	runClient();
		if (event.getActionCommand() == SERVER_BUTTON)	runServer();
	}

	private void runClient() {
		new ClientInterface();
		this.dispose();
	}
	
	private void runServer() {
		new ServerInterface();
	}
}