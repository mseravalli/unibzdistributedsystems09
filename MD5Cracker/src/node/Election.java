package node;

import java.util.ArrayList;

import cracker.Leader;
import cracker.QueueRecord;


public class Election extends Thread{
	

//	private ArrayList<RoutingRecord> routingTable;
			
	private RoutingRecord winner;
	
	private StringBuffer hashval;
	
//	private QueueRecord[] queue;
	
	public Election(StringBuffer hash){
		
		hashval = hash;
		
//		routingTable = rTable;
		
//		queue = aQueue;
	}
	
	public RoutingRecord getWinner(){
		return winner;
	}
	
	
	public RoutingRecord createNewID(){
		int size = Node.getRoutingTable().size();
		int id = (int) (Math.random()*(size*size*size));
		
		String address = "";
		int port = -1;
		
		//set the id of the current process to the new created ID
		for(int i = 0; i < Node.getRoutingTable().size(); i++){
			if(Node.getRoutingTable().get(i).isMe){
				Node.getRoutingTable().get(i).ID = id;
				address = Node.getRoutingTable().get(i).IP;
				port = Node.getRoutingTable().get(i).port;
			}
		}
		
		return new RoutingRecord(address, port, RoutingRecord.IS_NOT_ME, id);
		
	}
	
	
	//returns false if there is no unique winner, otherwhise returns true and sets the winner
	public boolean isUniqueMaxID(){
		
		//find the maximum id and its position
		int maxID = -1;
		int maxIDPos = 0;
		for(int i = 0; i < Node.getRoutingTable().size(); i++){
			if (Node.getRoutingTable().get(i).ID > maxID){
				maxID = Node.getRoutingTable().get(i).ID;
				maxIDPos = i;
			}
		}

		//check whether there is another ID in the list
		boolean isUnique = true;
		for(int i = 0; i < Node.getRoutingTable().size(); i++){
			if (Node.getRoutingTable().get(i).ID == maxID && i != maxIDPos){
				isUnique = false;
			}
		}
		
		if(isUnique){
			winner = Node.getRoutingTable().get(maxIDPos);
			winner.isLeader = true;
		}
		return isUnique;		
		
	}	
	
	
	public boolean isFilled(){
		
		for(int i = 0; i < Node.getRoutingTable().size(); i++){
			System.out.printf("nodes: %d - %d : %d\n", i, Node.getRoutingTable().get(i).port, Node.getRoutingTable().get(i).ID);
			if (Node.getRoutingTable().get(i).ID == node.Node.NULL_ID){
				return false;
			}
		}
		
		return true;
	}
	
	
	public void clearRTable(){
		
		for(RoutingRecord rr : Node.getRoutingTable()){
			rr.ID = Node.NULL_ID;
		}		
	}
	
	
	


	@Override
	public void run() {	
		
			do{
				
				System.out.println("broadcasting id");
				RoutingRecord toSend = this.createNewID();
				
				Node.broadcastObject(Node.getRoutingTable(), toSend);
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.getMessage();
					e.printStackTrace();
				}
				System.out.println("waiting for the ids");
				
				
				synchronized(Node.getRoutingTable()){
					
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
		Node.setIsElecting(false);
		Node.setHasLeader(true);
		
		
		//if it is the leader
		if(winner.isMe){
			System.out.println("I am the leader");
			new Leader(hashval, 65, 90, 4).start();
		}

		
	}	
	
	
}



