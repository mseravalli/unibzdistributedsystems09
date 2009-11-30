package server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

/*
 * Server implements Runnable in order to be able to execute the tests
 */
public class Server implements Runnable{

		ArrayList<Socket> clientList;
        ArrayList<String> nicknameList;
        boolean isClientLogged;
        DataInputStream in;
        DataOutputStream out;
        int serverPort;

		/**
		 * Constructs a new Server
		 */
        public Server(){

        	serverPort = 8080;
        	clientList = new ArrayList<Socket>();
            nicknameList = new ArrayList<String>();
            isClientLogged = false;                
            
        }
        
        /**
         * Constructs a new Server with the given port
         * @param port
         */
        public Server(int port){

        	serverPort = port;
        	clientList = new ArrayList<Socket>();
            nicknameList = new ArrayList<String>();
            isClientLogged = false;                
            
        }

		/**
		 * Accepts connections from clients, the nickname of a client is unique,
		 * if two clients try to connect with the same nickname, the connection
		 * with the second is dropped
		 */
        public void startServer(){
		try{

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
                            	isClientLogged = true;
                        }

                        /*
                         * if a nickname like the sent one is not present, it is added
                         * to the list and another thread is created otherwhise
                         * the client is informed and the connection is closed
                         */
                        if(!isClientLogged){
                        	clientList.add(clientSocket);
                            nicknameList.add(nick);
                            ServerConnection sc = new ServerConnection(clientSocket, clientList, nicknameList);
                        } else {
                            System.out.printf("the nickname %s is already chosen\n", nick);
                            out = new DataOutputStream( clientSocket.getOutputStream());
                            out.writeUTF("Nickname already chosen, please select another one and reconnect");

						    in.close();
						    out.close();

                            clientSocket.close();
                        }

                        isClientLogged = false;
                    }

                } catch(IOException e) {
                	System.out.println("Listen: " + e.getMessage());
	            }
        }
	
        /**
         * starts the server
         */
        @Override
    	public void run() {
    		this.startServer();    		
    	}
        
    
	public static void main (String args[]) { 
		Server server;
		
		if (args != null && args.length>0){
			server = new Server(Integer.parseInt(args[0]));
		} else 
			server = new Server();
        server.startServer();

	}

} 