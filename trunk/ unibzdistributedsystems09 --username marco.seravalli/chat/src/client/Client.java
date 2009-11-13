package client;

import java.net.*;
import java.io.*;


public class Client { 

	public static void main (String args[]) { 
    	
		String message = "message from client";
    	
		Socket socket = null;
	
		String hostName = "127.0.0.1";
		int serverPort = 8080;
	

		try{ 
	   
			socket = new Socket(hostName, serverPort);
			
			DataInputStream in = new DataInputStream( socket.getInputStream());
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
	    
	    
			out.writeUTF(message); // UTF is a string encoding 
			
			String data;
			

				data = in.readUTF(); 
				System.out.println("Received: "+ data);

			
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
}
