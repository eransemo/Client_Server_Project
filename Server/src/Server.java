import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//Constructor
	public Server(){
		super("IM program - Server");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	//setup and run the server
	public void startRunning(){
		try{
			server = new ServerSocket(6789, 100);
			while(true){
				try{
					//Connect and have conversation
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\n Server ended the connection!");
				}finally{
					closeAll();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			}
	}
	
	//wait for connection, then display information
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect... \n");
		connection = server.accept();
		showMessage(" Now Conncted to " + connection.getInetAddress().getHostName());
	}
	
	//get stream to send and recieve data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now setup! \n");
	}
	
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = "You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do{
			//have a conversation
			try{
				message  =(String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n I Don't know this object type \n");
			}
		}while(!message.equals("CLIENT - END"));
	}
	
	//close streams and sockets after done chatting
	private void closeAll(){
		showMessage("\n Closing Connections... \n");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioExcesptopn){
			ioExcesptopn.printStackTrace();
		}
	}
	
	//send message to client
	private void sendMessage(String message){
		try{
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}catch(IOException ioExceptopn){
			chatWindow.append("\n ERROR: I cant send that message");
		}
	}
	
	//updates chat window
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(text);
					}
				}
		);
	}
	
	//Let user to be able to type
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
		); 
	}
}
