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
	
	public ClientConnection(Socket socket){
		
		allMessages = null;
		
		serverSocket = socket;
		try {
			in = new DataInputStream(serverSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ClientConnection(Socket socket, JTextArea textArea){
		
		allMessages = textArea;
		
		serverSocket = socket;
		try {
			in = new DataInputStream(serverSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
	public void run(){
		try {
			
			if (allMessages != null)
				allMessages.setText("");
			else
				System.out.println("textArea not found!!");
			
			while(true){
		    
				String data = in.readUTF();
		    	
				if (allMessages != null){
					String history = allMessages.getText();
					history = history + data + "\n";
					allMessages.setText(history);
				} else {
					System.out.println(data);
				}			
				
		    	
			}
		    
		} catch(EOFException e) {
			System.out.println("EOF: "+e.getMessage()); 
		} catch(IOException e) {
			System.out.println("IO:s a"+e.getMessage());
		} finally {
		    try {
		    	serverSocket.close();
		    } catch (IOException e) {/*close failed*/}
		}
    }
	
	
	

}
