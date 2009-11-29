package client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientConnection extends Thread {
	
	DataInputStream in;
	
	JTextArea allMessages;

	Socket serverSocket;

	/**
	 * Constructs a new ClientConnection using the socket connected with the
	 * server, than ClientConnection initializes the dataInputStream using the
	 * passed socket.
	 * 
	 * @param aSocket
	 * @throws IOException
	 */
	public ClientConnection(Socket aSocket) throws IOException{

                serverSocket = aSocket;
                in = new DataInputStream(serverSocket.getInputStream());
		allMessages = null;

	}


	/**
	 * Constructs a new ClientConnection using the socket connected with the
	 * server, than ClientConnection initializes the dataInputStream using the
	 * passed socket,
	 * furtheremore, ClientConnection gets as parameter a JTextArea in order
	 * to print the output there rather than on the console
	 * 
	 * @param aSocket
	 * @param textArea
	 * @throws IOException
	 */
	public ClientConnection(Socket aSocket, JTextArea textArea) throws IOException{
		serverSocket = aSocket;
		in = new DataInputStream(serverSocket.getInputStream());		
		allMessages = textArea;
	
	}
	
	/**
	 * While is running the thread reads the inputStream and prints the output
	 * either on the console or on a JTextArea
	 */
	@Override
	public void run(){
		try {
			
			/*
			 * if no textArea is found it is notified to the user
			 */
			if (allMessages != null)
				allMessages.setText("");
			else
				System.out.println("textArea not found!!");
			
			while(true){
		    
				String data = in.readUTF();
		    	
				/*
				 * if no textArea is found the output will be printed on the console
				 * otherwise the textArea is updated 
				 */
				if (allMessages != null){
					allMessages.append(data+"\n");
				} else {
					System.out.println(data);
				}			
		    	
			}
		    
		} catch(EOFException e) {

                    if (allMessages != null) {
                        allMessages.append("Server Disconnected\n");
                    } else {
                        System.out.println("Server Disconnected");
                    }

                    try {

                        this.in.close();
                        this.serverSocket.close();
                        this.interrupt();
			
                    } catch (IOException ioe) {
                        System.out.println("ClientConnection IO: "+ioe.getMessage());
                    }
		} catch(IOException ioe) {
			System.out.println("Client Disconnected: "+ioe.getMessage());
		} 

	}
	
	
	

}
