package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends Thread {
	
	DataInputStream in; 

	Socket serverSocket;
	
	public ClientConnection(Socket socket){
		
		serverSocket = socket;
		try {
			in = new DataInputStream(serverSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}	
	
	public void run(){
		try {
			
			while(true){
		    
				String data = in.readUTF();
		    	System.out.println(data);
		    	
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
