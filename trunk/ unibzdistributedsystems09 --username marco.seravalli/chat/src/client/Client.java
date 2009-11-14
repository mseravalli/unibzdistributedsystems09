package client;

import java.net.*;
import java.util.Scanner;
import java.io.*;


public class Client {
	
	public Client(){
		
		
		
	}
	
	public void connect (){
		String message = "";
    	
		Socket socket = null;
	
		String hostName = "127.0.0.1";
		int serverPort = 8080;

		try{ 
	   
			socket = new Socket(hostName, serverPort);
			
			DataOutputStream out = new DataOutputStream( socket.getOutputStream());
	    
			int i = 0;
			
			ClientConnection cc = new ClientConnection(socket);
			cc.start();
			
			while(i<1000){
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
    	
		Client c = new Client ();
		c.connect();
		
	}
}
