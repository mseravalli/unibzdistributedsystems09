package client;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import javax.swing.JTextArea;


public class Client {
	
	String hostName;
	int serverPort;
	Socket socket;
	
	String message;
	JTextArea allMessages;
	
	/**
	 * set hostName to localhost (127.0.0.1) and serverPort to 8080
	 */
	public Client(){
		hostName = "127.0.0.1";
		serverPort = 8080;
		socket = null;
		allMessages = null;
	}
	
	/**
	 * set hostName to localhost (127.0.0.1) and serverPort to 8080 
	 * @param textArea
	 */
	public Client(JTextArea textArea){
		hostName = "127.0.0.1";
		serverPort = 8080;
		socket = null;
		allMessages = textArea;		
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 */
	public Client(String host, int port){
		hostName = host;
		serverPort = port;
		socket = null;
		allMessages = null;
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param textArea
	 */
	public Client(String host, int port, JTextArea textArea){
		hostName = host;
		serverPort = port;
		socket = null;
		allMessages = textArea;		
	}
	
	public void connect (){
    	

		try{ 
	   
			socket = new Socket(hostName, serverPort);
			
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
			
			ClientConnection cc = new ClientConnection(socket, allMessages);
			cc.start();
			
			while(true){
				Scanner sc = new Scanner(System.in);
				message = sc.nextLine();
				
				out.writeUTF(message); 			
			}			
			
			
		} catch (UnknownHostException e){ 
			
			System.out.println("Sock: " + e.getMessage());
		
		} catch (EOFException e){
			
			System.out.println("EOF: " + e.getMessage()); 
		
		} catch (IOException e){
			
			System.out.println("IO: " + e.getMessage());
		
		} finally {
			
			if(socket!=null)			
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("" + e.getMessage());
				}
		
		}
	}

	public static void main (String args[]) { 
    	
		Client c = new Client();
		c.connect();
		
	}
}
