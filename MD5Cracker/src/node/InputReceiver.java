package node;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Node.HelloPacket;

import cracker.RoutingRecord;

public class InputReceiver extends Thread {
	
	private ObjectInputStream in;
	private ArrayList <RoutingRecord> routingTable;
	
	public InputReceiver(ObjectInputStream inStr, ArrayList <RoutingRecord> rTable){
		in = inStr;
		routingTable = rTable;
	}
	
	
	public void run(){		
		
		System.out.println("InputReceiver: ready to receive data");
		
		try { 
			
			while(true){
				
		    
				Object o = in.readObject();
		    	
			}
		    
		} catch(EOFException e) {
                    
			System.out.println("node disconnected");
			
//			int i = this.clients.indexOf(this.clientSocket);
//			System.out.println("Client "+this.nicknames.get(i)+" disconnected");
//			this.nicknames.remove(i);
//			this.clients.remove(this.clientSocket);
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
