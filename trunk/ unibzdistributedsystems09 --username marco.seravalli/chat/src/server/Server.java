package server;

import java.net.*;
import java.util.Scanner;
import java.io.*;


public class Server {
    
	public static void main (String args[]) { 
		
    	try{
		    
    		int serverPort = 8080; 
		    ServerSocket listenSocket = new ServerSocket(serverPort); 
		    
		    while(true) {
		    	Socket clientSocket = listenSocket.accept();
		    	System.out.printf("Nuovo client attivo!!\n");
		    	Connection c = new Connection(clientSocket);
		    }
		    
		} catch(IOException e) {
			System.out.println("Listen: " + e.getMessage());
		}
	}

} 

class Connection extends Thread {
	
    DataInputStream in;
    DataOutputStream out; 
    
    Socket clientSocket;
    
    public Connection (Socket aClientSocket) { 
    	
		try {
			clientSocket = aClientSocket; 
		    in = new DataInputStream(clientSocket.getInputStream()); 
		    out = new DataOutputStream( clientSocket.getOutputStream()); 
		    
		    this.start();
		    
		} catch(IOException e) {
			System.out.println("Connection: " + e.getMessage());
		}
    } 

    public void run(){
		try { // an echo server 
			
			while(true){
		    
				String data = in.readUTF();
		    	System.out.println(data);
		    		    
		    	out.writeUTF(data);
			}
		    
		} catch(EOFException e) {
			System.out.println("EOF: "+e.getMessage()); 
		} catch(IOException e) {
			System.out.println("IO:s a"+e.getMessage());
		} finally {
		    try {
		    	clientSocket.close();
		    } catch (IOException e) {/*close failed*/}
		}
    }
}