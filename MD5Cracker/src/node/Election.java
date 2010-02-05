package node;

import java.util.ArrayList;

import cracker.Leader;
import cracker.QueueRecord;


public class Election extends Thread{
	

	private ArrayList<RoutingRecord> routingTable;
			
	private RoutingRecord winner;
	
	private StringBuffer hashval;
	
	private boolean[] isElecting;
	
	private boolean[] hasLeader;
	
	private QueueRecord[] queue;
	
	public Election(ArrayList<RoutingRecord> rTable, StringBuffer hash, boolean[] iE, boolean[] hL, QueueRecord[] aQueue){
		
		isElecting = iE;		
		hasLeader = hL;
		
		hashval = hash;
		
		routingTable = rTable;
		
		queue = aQueue;
	}
	
	public RoutingRecord getWinner(){
		return winner;
	}
	
	
	public RoutingRecord createNewID(){
		int id = (int) (Math.random()* 2);//(routingTable.size()*routingTable.size()*routingTable.size()));
		
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
		
		if(isUnique){
			winner = routingTable.get(maxIDPos);
			winner.isLeader = true;
		}
		return isUnique;		
		
	}	
	
	
	public boolean isFilled(){
		
		for(int i = 0; i < routingTable.size(); i++){
			System.out.printf("nodes: %d - %d : %d\n", i, routingTable.get(i).port, routingTable.get(i).ID);
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
	}
	
	
	


	@Override
	public void run() {	
		
			do{
				
				System.out.println("broadcasting id");
				RoutingRecord toSend = this.createNewID();
				
				Node.broadcastObject(routingTable, toSend);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.getMessage();
					e.printStackTrace();
				}
				System.out.println("waiting for the ids");
				
				
				synchronized(this.routingTable){
					
					if(isFilled()){
					
						System.out.println("All nodes have an id");
						
						if(isUniqueMaxID()){
							break;
						} else {
							System.out.println("there are nodes that have the same max id");
							clearRTable();
						}				
						
					}	
				
				}
				
			}while(true);

			
			
		
		System.out.println("The winner is " + winner.IP + ":" + winner.port);
		
		//sets the boolean variables isElecting and hasLeader
		isElecting[0] = false;
		hasLeader[0] = true;
		
		//if it is the leader
		if(winner.isMe){
			System.out.println("I am the leader");
//			public Leader(String hashString, int first, int last, int freeChars, boolean[] computing){
			new Leader(routingTable, hashval, 65, 90, 4, hasLeader, queue).start();
		}

		
	}	
	
	
}



