package client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class ClientConnection extends Thread {
	
	DataInputStream in;
	
	JTextArea allMessages;

	Socket serverSocket;
	
	public ClientConnection(Socket aSocket, JTextArea textArea) throws IOException{

                serverSocket = aSocket;

                in = new DataInputStream(serverSocket.getInputStream());
		allMessages = textArea;
	
	}
	
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
                    } catch (IOException ex) {
                        System.out.println("ClientConnection IO:s a"+e.getMessage());
                    }
		} catch(IOException e) {
			System.out.println("ClientConnection IO:s a"+e.getMessage());
		} 
    }
	
	
	

}
