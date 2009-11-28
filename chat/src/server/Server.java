package server;

import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class Server {

        ArrayList<Socket> clientList;
        ArrayList<String> nicknameList;
        boolean isLogged;
        DataInputStream in;
        DataOutputStream out;

	/**
	 *
	 */
        public Server(){

		clientList = new ArrayList<Socket>();
                nicknameList = new ArrayList<String>();

                isLogged = false;                
            
        }

	/**
	 * 
	 */
        public void start(){
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

			    in.close();
			    out.close();

                            clientSocket.close();
                        }

		    	isLogged = false;
                    }

                } catch(IOException e) {
			System.out.println("Listen: " + e.getMessage());
            }
        }
	
	
    
	public static void main (String args[]) { 
		
		Server server = new Server();
                server.start();

	}

} 