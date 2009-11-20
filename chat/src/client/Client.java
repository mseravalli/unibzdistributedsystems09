package client;

import java.net.*;
import java.io.*;

import javax.swing.JTextArea;


public class Client {

	private String hostName;
	private int serverPort;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String message;
	private JTextArea allMessages;
        ClientConnection cc;
	
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
	
	
	
	public Socket getSocket(){
		return this.socket;
	}
	
	public void setHostName(String host){
		this.hostName = host;
	}

	public void setServerPort(int port){
		this.serverPort = port;
	}
	
	public boolean connect (String serverIP, int port){
    	boolean success = true;
    	hostName = serverIP;
    	serverPort = port;
		try{ 
	   
			socket = new Socket(serverIP, serverPort);
			
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			cc = new ClientConnection(socket, allMessages);
			cc.start();		
			
			
		} catch (UnknownHostException e){ 
			
			success = false;
			System.out.println("Sock: " + e.getMessage());
		
		} catch (EOFException e){
			
			success = false;
			//System.out.println("");
                        if (allMessages != null){
				allMessages.append("Server Disconnected");
			} else {
				System.out.println("Server Disconnected");
			}
		
		} catch (IOException e){
			
			success = false;
			System.out.println("IO: " + e.getMessage());
		
		} finally {
		
		}
		
		return success;
	}
	
	
	/**
         *
         * The method sends the passed message to the server to which the client
         * is connected
         *
         * @param message
         * @return
         */
	public boolean sendMessage(String message){
		try {
                        if(!this.socket.isClosed())
                            out.writeUTF(message);
                        else {
                            if (allMessages != null) {
                                allMessages.append("No more connected with any server\n");
                            } else {
                                System.out.println("No more connected with any server");
                            }
                        }
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	
	
	public void disconnect(){
		try {

			out.close();
			in.close();
			this.socket.close();
			this.socket = null;
                        //the thread is stopped 
                        cc.interrupt();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String args[]) { 
    	
		Client c = new Client();
		c.connect("127.0.0.1", 8080);
		
	}
}
