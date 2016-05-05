import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	// Constructor
	public Client(String host) {
		super("IM program - Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userText.setText("");
			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	// Connect to Server!//
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated the connection \n");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			closeAll();
		}
	}

	// Connect to a Server
	private void connectToServer() throws IOException {
		showMessage("\n Attempting Connection...... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("\n Connected to: " + connection.getInetAddress().getHostName());
	}

	// set up streams to send and receive messages
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Your streams are now good to gow! \n");
	}

	// while chatting with server
	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException classNoFoundException) {
				showMessage("\n I Don't know this object type");
			}
		} while (!message.equals("SERVER - END"));
	}

	// Close the Streams and sockets
	private void closeAll() {
		showMessage("\n Closing All... \n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Send message to server
	private void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n ERROR: I cant send that message");
		}
	}

	// change and update chat window
	private void showMessage(final String m) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(m);
			}
		});
	}
	
	//Gives users permission to type into the text box
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}
}
