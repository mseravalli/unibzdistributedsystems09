package node;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class InputReceiver extends Thread {
	
	private boolean[] isElecting;
	private boolean[] hasLeader;
	
	private StringBuffer hashval;
	private String ip;
	private int port;
	
	private Socket socket;
	
	private ObjectInputStream in;
	private ArrayList <RoutingRecord> routingTable;
	
	public InputReceiver(String addr, int portNum, Socket aSocket, ArrayList <RoutingRecord> rTable, boolean[] electing, boolean[] working, StringBuffer hash){
		
		isElecting = electing;
		hasLeader = working;
		
		hashval = hash;
		ip = addr;
		port = portNum;
		socket = aSocket;
		in = null;
		routingTable = rTable;
	}
	
	public void checkString(String toParse){
		
		//part for election
		if(toParse.startsWith(Node.ELECTION)){
			isElecting[0] = true;
			System.out.println("election started!!");
			hashval = new StringBuffer(toParse.substring(toParse.indexOf(":")+1));
			isElecting[0] = true;
			hasLeader[0] = false;
			Election el = new Election(routingTable,hashval,isElecting,hasLeader);
			el.start();
			
		}
		
	}
	
	
	public void updateTable(RoutingRecord record){
		
		for(RoutingRecord rr : routingTable){
			
			if(rr.IP.equals(record.IP) && rr.port == record.port){
				rr.ID = record.ID;
			}
			
		}
		
	}
	
	
	public void run(){		
		
		System.out.printf("InputReceiver: ready to receive data from: %s - %d\n", socket.getInetAddress(),  socket.getPort());
		
		try { 
			
			while(true){
				
				in = new ObjectInputStream(socket.getInputStream());
		    
				Object o = in.readObject();
//				System.out.println(" i received a " + o.getClass().toString());
				//if the received object is a string
				if(o.getClass().equals(String.class)){
					
					checkString((String)o);
				
				//if the received object is a routing record
				} else if (o.getClass().equals(RoutingRecord.class)){
					System.out.printf("%d - %d\n",((RoutingRecord)o).port, ((RoutingRecord)o).ID);
					updateTable((RoutingRecord)o);
				}
				
		    	
			}
			
		    
		} catch(EOFException e) {
                    
			System.out.printf("node %s:%d disconnected and deleted from routing table\n", ip, port);
			
			int position = -1;
			
			for(int i = 0; i < routingTable.size(); i++){
				if(routingTable.get(i).IP.equals(ip) && routingTable.get(i).port == port){
					position = i;
				}
					
			}
			
			if(routingTable.get(position).isLeader){
				routingTable.remove(position);
				//TODO inserire la hash nel metodo
				new Election(routingTable, hashval,isElecting,hasLeader).start();				
			} else {
				routingTable.remove(position);
			}
			
			
		} catch (SocketException e) {
			System.out.printf("node %s:%d disconnected\n", ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
