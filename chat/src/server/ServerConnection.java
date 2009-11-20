package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConnection extends Thread {
	
    DataInputStream in;
    DataOutputStream out; 
    
    Socket clientSocket;
    ArrayList<Socket> clients;
    
    public ServerConnection (Socket aClientSocket, ArrayList<Socket> clientList) { 
    	
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

    @Override
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
                    
			System.out.println("Client disconnected");
			this.clients.remove(this.clientSocket);
			

		} catch(IOException e) {
			System.out.println("Server Connection IO:s a"+e.getMessage());
		} finally {
		    try {
		    	clientSocket.close();
		    } catch (IOException e) {
		    	System.out.println("Server Connection IO:s a"+e.getMessage());
		    }

                    this.stop();

		}
    }
}