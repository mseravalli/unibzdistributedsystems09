package cracker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Election {

	ArrayList<RoutingRecord> routingTable;
	
	ObjectOutputStream oos;
	
	Socket socket;
	
	public Election(ArrayList<RoutingRecord> rTable, Socket aSocket){
		routingTable = rTable;
		socket = aSocket;
	}
	
	public void broadCastID(){
		
		int id = (int) (Math.random() * (routingTable.size()*routingTable.size()));
		
		//set the id of the current process to
		for(RoutingRecord rr : routingTable){
		
			if(rr.IP.equals(socket.getInetAddress().toString())){
				rr.ID = id;
			}
			
		}
		
		try {
			
			//TODO really broadcast
			oos = new ObjectOutputStream(socket.getOutputStream());
			
			oos.writeObject(routingTable);
			
			
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
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
		super(rTable, aSocket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {		
		
		do{
			
			super.broadCastID();
			
			//read until the table is completely filled
			do{
				//TODO
			}while(!super.isFilled());
			
			//if there is no unique max id clear the routing table and wait until
			//it is completely clean
			if(!isUniqueMaxID()){
				super.clearRTable();
				
				//read table until it is clean
				do{
					//TODO
				}while(!super.isClean());				
			}			
			
		} while(!isUniqueMaxID());
		
		
		//we have a leader!!
		
		
	}	
	
	
}



