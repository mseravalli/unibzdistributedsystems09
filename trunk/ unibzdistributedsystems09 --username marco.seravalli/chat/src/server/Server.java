package server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class Server {
	
	
    
	public static void main (String args[]) { 
		
		ArrayList<Socket> clientList = new ArrayList<Socket>();
		
		
    	try{
		    
    		int serverPort = 8080; 
		    ServerSocket listenSocket = new ServerSocket(serverPort); 
		    
		    Socket clientSocket;
		    
		    System.out.println("sever started");
		    
		    while(true) {
		    	clientSocket = listenSocket.accept();
		    	System.out.printf("new client connected!!\n");
		    	clientList.add(clientSocket);
		    	Connection c = new Connection(clientSocket, clientList);
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
    ArrayList<Socket> clients;
    
    public Connection (Socket aClientSocket, ArrayList<Socket> clientList) { 
    	
		try {
			clientSocket = aClientSocket;
			clients = clientList;
			
		    in = new DataInputStream(clientSocket.getInputStream());
		    //out = new DataOutputStream( clientSocket.getOutputStream()); 
		    
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
		    	
		    	
		    	for(int i = 0; i<clients.size();i++){
		    		//clients.get(i);
		    		
		    		out = new DataOutputStream( clients.get(i).getOutputStream());
		    		out.writeUTF(data);
		    	}
		    	//out.writeUTF(data);
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