package node;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;


public class InputReceiver extends Thread {
	
	private static final String NOT_POSESSOR = "";
	private boolean[] isElecting;
	private boolean[] isWorking;
	
	private String ip;
	private int port;
	
	private Socket socket;
	
	private ObjectInputStream in;
	private ArrayList <RoutingRecord> routingTable;
	
	public InputReceiver(String addr, int portNum, Socket aSocket, ArrayList <RoutingRecord> rTable, boolean[] electing, boolean[] working){
		
		isElecting = electing;
		isWorking = working;
		
		ip = addr;
		port = portNum;
		socket = aSocket;
		in = null;
		routingTable = rTable;
	}
	
	public void checkString(String toParse){
		
		//part for election
		if(toParse.equals(Node.ELECTION)){
			isElecting[0] = false;
			System.out.println("election started!!");
			
			Election el = new Election(routingTable,NOT_POSESSOR);
			el.start();
			
		// part for hash exchanging
		//TODO improve the parser and start the work
		} else if(true){
			System.out.println("hash received: " + toParse);
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
				if(o.getClass().toString().equals("class java.lang.String")){
					
					checkString((String)o);
				
				//if the received object is a routing record
				} else if (o.getClass().toString().equals("class cracker.RoutingRecord")){
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
			
			routingTable.remove(position);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
