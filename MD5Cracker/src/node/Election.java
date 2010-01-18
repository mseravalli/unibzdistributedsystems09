package node;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Election extends Thread{
	

	private ArrayList<RoutingRecord> routingTable;
	
	private ObjectOutputStream out;
	
	private Socket socket;
	
	private RoutingRecord winner;
	
	private String hash;
	
	public Election(ArrayList<RoutingRecord> rTable, String aHash){
		
		hash = aHash;
		
		routingTable = rTable;

		socket = new Socket();
	}
	
	public RoutingRecord getWinner(){
		return winner;
	}
	
	
	public RoutingRecord createNewID(){
		int id = (int) (Math.random()* (routingTable.size()*routingTable.size()));
		
		String address = "";
		int port = -1;
		
		//set the id of the current process to the new created ID
		for(int i = 0; i < routingTable.size(); i++){
			if(routingTable.get(i).isMe){
				routingTable.get(i).ID = id;
				address = routingTable.get(i).IP;
				port = routingTable.get(i).port;
			}
		}
		
		return new RoutingRecord(address, port, RoutingRecord.IS_NOT_ME, id);
		
	}
	
	
	public void broadcastRoutingRecord(RoutingRecord rrToSend){		
		
		//the method sends the id to all the connected nodes
		try {
			for(RoutingRecord rr : routingTable){
				if(!rr.isMe){
					out = new ObjectOutputStream(rr.socket.getOutputStream());
					out.writeObject(rrToSend);
					out.flush();
				}			
			}
		} catch (SocketException e) {
			System.out.printf("Election: node disconnected\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//returns false if there is no unique winner, otherwhise returns true and sets the winner
	public boolean isUniqueMaxID(){
		
		//find the maximum id and its position
		int maxID = -1;
		int maxIDPos = 0;
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID > maxID){
				maxID = routingTable.get(i).ID;
				maxIDPos = i;
			}
		}
		
		//check whether there is another ID in the list
		boolean isUnique = true;
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID == maxID && i != maxIDPos){
				isUnique = false;
			}
		}
		
		if(isUnique)
			winner = routingTable.get(maxIDPos);
		return isUnique;		
		
	}	
	
	
	public boolean isFilled(){
		
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID == node.Node.NULL_ID){
				return false;
			}
		}
		
		return true;
	}
	
	
	public void clearRTable(){
		
		for(RoutingRecord rr : routingTable){
			rr.ID = Node.NULL_ID;
		}
		
		
//		String address = "";
//		int port = -1;
//		
//		//set the id of the current process to the new created ID
//		for(int i = 0; i < routingTable.size(); i++){
//			if(routingTable.get(i).isMe){
//				routingTable.get(i).ID = Node.NULL_ID;
//				address = routingTable.get(i).IP;
//				port = routingTable.get(i).port;
//			}
//		}
//		
//		broadcastRoutingRecord(new RoutingRecord(address, port, RoutingRecord.IS_NOT_ME, Node.NULL_ID));
		
	}
	
	
	public boolean isClean(){
		
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID != -1){
				return false;
			}
		}
		
		return true;
	}
	


	@Override
	public void run() {	
		
		
		do{
			
			System.out.println("broadcasting id");
			
			broadcastRoutingRecord(this.createNewID());
			
			do{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.getMessage();
					e.printStackTrace();
				}
				System.out.println("waiting for the ids");
			}while(!isFilled());
			
			System.out.println("All nodes have an id");

			
//			
			if(!isUniqueMaxID()){
//				
//				System.out.println("there are 2 nodes that have the same max id");
//				
//				//wait until table until it is clean
//				do{
					clearRTable();
//					System.out.println("waiting for a clean table");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.getMessage();
//						e.printStackTrace();
//					}
//				}while(!isClean());	
//				
//				System.out.println("table is clean everywhere");
//				
			}

			
			
		}while(!isUniqueMaxID());
		System.out.println("The winner is " + winner.IP + ":" + winner.port);
		
		//if this is the hash possessor and not the leader he sends the hash to the leader
		if(!hash.equals("") && !winner.isMe){
			try {
				new ObjectOutputStream(winner.socket.getOutputStream()).writeObject(hash);
				System.out.println("I am sending the hash to the leader");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//else if this is the hash possessor and the leader it starts to "lead"
		else if(!hash.equals("") && winner.isMe){
			System.out.println("I AM THE LEADER & Already have the hash");
			//TODO  start the leading
		}
		
	}	
	
	
}



