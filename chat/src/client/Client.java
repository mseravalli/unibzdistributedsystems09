package client;

import java.net.*;
import java.io.*;

import javax.swing.JTextArea;


public class Client {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private JTextArea allMessages;
        ClientConnection cc;
	
	/**
	 * Constructs a new Client
	 */
	public Client(){
		socket = null;
		allMessages = null;
		in = null;
		out = null;
		cc = null;
	}
	
	/**
	 * Constructs a new Client, it gets as parameter a JTextArea in order to
	 * print the output there rather than on the console
	 * 
	 * @param textArea
	 */
	public Client(JTextArea textArea){
		socket = null;
		allMessages = textArea;
		in = null;
		out = null;
		cc = null;
	}

	/**
	 * The client connects to the server, the connection parameters such as
	 * the IP address and the port are passed to the method each time the client
	 * has to connect, the nickname is also required, because the fist message
	 * that is passed to the server is actually the nickname
	 *
	 * @param serverIP
	 * @param serverPort
	 * @param nickname
	 * @return true if the connection is set correctly
	 */
	public boolean connect (String serverIP, int serverPort, String nickname){

		boolean success = true;

		try{
	   
			socket = new Socket(serverIP, serverPort);
			
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

            //the client sends its nickname
            out.writeUTF(nickname);

            /*
             * after the sending of the name Client connection starts
             */
			cc = new ClientConnection(socket, allMessages);
			cc.start();		
			
			
		} catch (UnknownHostException e){ 
			
			success = false;
			System.out.println("Client Sock: " + e.getMessage());
		
		} catch (EOFException e){
			
			success = false;
            if (allMessages != null){
				allMessages.append("Server Disconnected");
			} else {
				System.out.println("Server Disconnected");
			}
		
		} catch (IOException ioe){
			
			success = false;
            if (allMessages == null)
            	System.out.println("Client IO: " + ioe.getMessage());
		
		}
        
		return success;
	}
	
	
	/**
         *
         * The method sends the passed message to the server connected
         *
         * @param message
         * @return true if the message has been sent successfully
         */
	public boolean sendMessage(String message){
		try {
			/*
			 * if the socket exists and it is connected the message is sent
			 */
			if(socket!=null && !this.socket.isClosed()){
				out.writeUTF(message);
				return true;
			} else {
				if (allMessages != null) {
					allMessages.append("No more connected with any server\n");
				} else {
					System.out.println("No more connected with any server");
				}
                            
				return false;
			}
		} catch (IOException ioe) {
			System.out.println("Client IO: " + ioe.getMessage());
			return false;
		}
	}
	
	
	/**
	 * The method disconnects from the server and it closes the I/O streams
	 * and the socket, if they are open, then the thread ClientConnection is
	 * stopped too, if it were running
	 */
	public void disconnect(){
		try {

			/*
			 * if the streams are opened they will be closed
			 */
			if(out != null)
			    out.close();

			if(in != null)				
			    in.close();

			if(socket != null && !socket.isClosed()){
			    this.socket.close();
			    this.socket = null;
			}

            //the thread ClientConnection is stopped 
			if(cc != null)
			cc.interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String args[]) { 
    	
		Client c = new Client();
		c.connect("127.0.0.1", 8080, "nickname");
		
	}
}
