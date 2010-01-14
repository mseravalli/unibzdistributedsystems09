package cracker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Election {

	private ArrayList<RoutingRecord> routingTable;
	
	private ObjectOutputStream oos;
	
	private String ip;
	private int port;
	
	private Socket socket;
	
	public Election(ArrayList<RoutingRecord> rTable, String ipAddress, int portNumber){
		routingTable = rTable;
		this.ip = ipAddress;
		this.port = portNumber;
		socket = new Socket();
	}
	
	public void broadCastID(){
		
		int id = (int) (Math.random() * (routingTable.size()*routingTable.size()));
		
		//set the id of the current process to
		for(RoutingRecord rr : routingTable){
		
			if(rr.isMe){
				rr.ID = id;
			}
			
		}
		
	}
	
	
	
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
		
		return isUnique;		
		
	}	
	
	
	public boolean isFilled(){
		
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID == -1){
				return false;
			}
		}
		
		return true;
	}
	
	
	public void clearRTable(){
		
		for(int i = 0; i < routingTable.size(); i++){
			routingTable.get(i).ID = -1;
		}
		
		//TODO broadcast table
		
	}
	
	
	public boolean isClean(){
		
		for(int i = 0; i < routingTable.size(); i++){
			if (routingTable.get(i).ID != -1){
				return false;
			}
		}
		
		return true;
	}
	
}


class ElectionConnection extends Election implements Runnable {

	public ElectionConnection(ArrayList<RoutingRecord> rTable, Socket aSocket) {
		super(rTable, "127.0.0.1", 4334);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {		
		
		do{
			
			super.broadCastID();
			
			//wait until the table is completely filled
			do{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.getMessage();
					e.printStackTrace();
				}
				System.out.println("waiting for the ids");
			}while(!super.isFilled());
			
			System.out.println("every node has an id");
			
			//if there is no unique max id clear the routing table and wait until
			//it is completely clean
			if(!isUniqueMaxID()){
				super.clearRTable();
				System.out.println("there are 2 nodes that have the same max id" +
						"the ids will be cleared");
				
				//wait until table until it is clean
				do{
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.getMessage();
						e.printStackTrace();
					}
				}while(!super.isClean());				
			}			
			
		} while(!isUniqueMaxID());
		
		System.out.println("we have the leader;");
		
		//we have a leader!!
		
		
	}	
	
	
}



