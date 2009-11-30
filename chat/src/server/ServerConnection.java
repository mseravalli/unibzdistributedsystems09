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
    ArrayList<String> nicknames;

    /**
     * Constructs a new ServerConnection with the specified Socket, clientList and
     * nicknameList, the inputStream is initialized using the passed socket
     *
     * @param aClientSocket
     * @param clientList
     * @param nickList
     */
    public ServerConnection (Socket aClientSocket, ArrayList<Socket> clientList, ArrayList<String> nickList) {
    	
		try {
			clientSocket = aClientSocket;
			clients = clientList;
            nicknames = nickList;
			
			in = new DataInputStream(clientSocket.getInputStream());
		    
			this.start();
		    
		} catch(IOException e) {
			System.out.println("ServerConnection: " + e.getMessage());
		}
    } 

    
    /**
     * While running, ServerConnection receives a message form the connected client
     * and sends it to all Sockets contained in the clientList
     */
    @Override
    public void run(){
		try { 
			
			while(true){
		    
				String data = in.readUTF();
				System.out.println(data);
				/*
				 * adds at the beginning of the message the nickname of the sender
				 */
				int index = this.clients.indexOf(clientSocket);
				data = this.nicknames.get(index) +": " + data;

				/*
				 * the message is sent so each client present in the clients list
				 * for each client it is created a new outputStream
				 */
				for(int i = 0; i<clients.size();i++){
					
					out = new DataOutputStream( clients.get(i).getOutputStream());
					out.writeUTF(data);

				}
		    	
			}
		    
		} catch(EOFException e) {
                    
			System.out.println("Client disconnected");
                        int i = this.clients.indexOf(this.clientSocket);
                        this.nicknames.remove(i);
			this.clients.remove(this.clientSocket);
			

		} catch(IOException e) {
			System.out.println("ServerConnection IO:s a"+e.getMessage());
		} finally {
		    try {
		    	clientSocket.close();
		    } catch (IOException e) {
		    	System.out.println("ServerConnection IO:s a"+e.getMessage());
		    }
		    
                    //the thread is stopped
                    this.interrupt();

		}
    }
}