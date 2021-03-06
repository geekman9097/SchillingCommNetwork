package org.schillingSchool.communications.userInterface;

import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import org.schillingSchool.communications.client.*;
import org.schillingSchool.communications.utils.Utils;

public class ClientInterface extends JFrame implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final private static int TEXT_HISTORY = 10;
	final private static int DEFAULT_WIDTH = 20;
	final private static String TITLE = "Schilling Comm Network Client";
	final private static String PROMPT_TEXT = "Type a message Here";
	final private static String NEW_LINE = "\n";
	private static final String EXIT_CONFIRMATION = "Are you sure you want to close your client?";
	private static final String SLEEP_MESSAGE = "AFK";
	private static final String WAKE_MESSAGE = "Back";

	private JTextArea displayText;
	private JTextField typeBox;
	private Client myClient;
	private ClientOutThread myClientOutput;
	private boolean clientInitializing = true;
	private GridBagConstraints constraints;

	public ClientInterface() {
		setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		setClientLayout();
		setTitle(TITLE);
		setSize(getMaximumSize());
		setVisible(true);
		startClient();
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void clientFinishedStarting(ClientOutThread clientOutput) {
		clientInitializing = false;
		myClientOutput = clientOutput;
	}

	private void setClientLayout() {
		displayText = new JTextArea(TEXT_HISTORY, DEFAULT_WIDTH);
		JScrollPane scrollPane = new JScrollPane(displayText);
		displayText.setLineWrap(true);
		displayText.setWrapStyleWord(true);
		displayText.setEditable(false);
		displayText.setText("");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0.5;
		constraints.weightx = 0.5;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		add(scrollPane, constraints);

		typeBox = new JTextField(PROMPT_TEXT);
		typeBox.setEditable(true);
		typeBox.addActionListener(this);
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(typeBox, constraints);
	}

	public void displayMessage(String message) {
		displayText.append(message + NEW_LINE);
		displayText.setCaretPosition(displayText.getDocument().getLength());//jump the cursor to the end of the display to show it
	}

	private void startClient() {
		myClient = new Client(this);
		Thread execute = new Thread(myClient);
		execute.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) { //activated when a user hits enter on the text box
		String outStr = typeBox.getText(); //set our outString to be equal to what has been typed
		sendMessage(outStr);
		System.out.println("message Sent: " + outStr);
		typeBox.setText(""); //clear the text box after a message has been sent
		if (clientInitializing) {
			sendMessage(outStr);
			displayMessage(outStr); //display the outString. debug code.
		} else {
			sendMessage(outStr);
		}
	}
	
	private void sendMessage(String message) {
		if(clientInitializing) {
			myClient.setUserString(message);
			Utils.getLogger().info("Set User String" + this);
		} else {
			myClientOutput.setUserString(message);
		}
	}
	
	//when attempting to close, ask for confirmation of close
	synchronized private void closeClient() {
		System.out.println("Closing Client");
		int confirm = JOptionPane.showOptionDialog(
				this, EXIT_CONFIRMATION, 
				"Exit Confirmation", JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) { //confirm close
			myClient.end();
			this.dispose();
		}
	}

	@Override public void windowClosed(WindowEvent e) {}
	@Override public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		closeClient();
	}
	
	@Override public void windowIconified(WindowEvent e) {}
	@Override public void windowDeiconified(WindowEvent e) {}
	@Override public void windowActivated(WindowEvent e) {
		sendMessage(WAKE_MESSAGE);
	}
	@Override public void windowDeactivated(WindowEvent e) {
		sendMessage(SLEEP_MESSAGE);
	}
}