package server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class Server {
	
	
    
	public static void main (String args[]) { 
		
		ArrayList<Socket> clientList = new ArrayList<Socket>();
                ArrayList<String> nicknameList = new ArrayList<String>();

                boolean isLogged = false;

                DataInputStream in;
                DataOutputStream out;
		
    	try{
		    
    		int serverPort = 8080; 
		    ServerSocket listenSocket = new ServerSocket(serverPort); 
		    
		    Socket clientSocket;
		    
		    System.out.println("sever started");
		    
		    while(true) {
		    	clientSocket = listenSocket.accept();		    	

                        //the server reads the nickname send by the client
                        in = new DataInputStream(clientSocket.getInputStream());
                        String nick = in.readUTF();
                        System.out.printf("new client connected, nickname: %s \n", nick);

                        /*
                         * the server checks if the nickname sent is already present
                         * in the list of the nicknames
                         */
                        for(int i=0; i<nicknameList.size();i++){
                            if(nicknameList.get(i).equals(nick))
                                isLogged = true;
                        }

                        /*
                         * if a nickname like the sent one is not present, it is added
                         * to the list and another thread is created otherwhise
                         * the client is informed and the connection is closed
                         */
                        if(!isLogged){                            
                            clientList.add(clientSocket);
                            nicknameList.add(nick);
                            ServerConnection sc = new ServerConnection(clientSocket, clientList, nicknameList);
                        } else {
                            System.out.printf("the nickname %s is already chosen\n", nick);
                            out = new DataOutputStream( clientSocket.getOutputStream());
                            out.writeUTF("Nickname already chosen, please select another one and reconnect");
                            clientSocket.close();
                        }

		    	isLogged = false;
		    }
		    
		} catch(IOException e) {
			System.out.println("Listen: " + e.getMessage());
		}
	}

} 

/*
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
		    } catch (IOException e) {/*close failed}
		}
    }
}*/