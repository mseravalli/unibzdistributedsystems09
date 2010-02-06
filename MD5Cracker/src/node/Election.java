package node;

import cracker.Leader;

public class Election extends Thread{
			
	private RoutingRecord winner;
	
	private StringBuffer hashval;
	
	public Election(StringBuffer hash){
		
		hashval = hash;
		
	}
	
	/**
	 * 
	 * @return
	 */
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
	
	
	/**
	 * returns false if there is no unique winner, otherwise returns true and sets the winner
	 * @return
	 */
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
	
	/**
	 * Checks whether every node has sent an id
	 * 
	 * @return
	 */
	public boolean isFilled(){
		
		for(int i = 0; i < Node.getRoutingTable().size(); i++){
			System.out.printf("nodes: %d - %d : %d\n", i, Node.getRoutingTable().get(i).port, Node.getRoutingTable().get(i).ID);
			if (Node.getRoutingTable().get(i).ID == node.Node.NULL_ID){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * set every id as null
	 */
	public void clearRTable(){
		
		for(RoutingRecord rr : Node.getRoutingTable()){
			rr.ID = Node.NULL_ID;
		}		
	}
	
	
	

	/**
	 * waits until there is a leader, if the current node is the leader then
	 * a new leader is created and started
	 */
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
			new Leader(hashval).start();
		}

		
	}	
	
	
}



