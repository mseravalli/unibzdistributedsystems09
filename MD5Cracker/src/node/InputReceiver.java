package node;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import cracker.Leader;
import cracker.SendingStrings;
import cracker.QueueRecord;
import cracker.StringChecker;


public class InputReceiver extends Thread {
	
	private boolean[] isElecting;
	private boolean[] hasLeader;
	
	private StringBuffer hashval;
	private String ip;
	private int port;
	
	private Socket socket;
	
	private ObjectInputStream in;
	private ArrayList <RoutingRecord> routingTable;
	
	private QueueRecord[] queue;
	
	public InputReceiver(String addr, int portNum, Socket aSocket, ArrayList <RoutingRecord> rTable, boolean[] electing, boolean[] working, StringBuffer hash, QueueRecord[] aQueue){
		
		isElecting = electing;
		hasLeader = working;
		
		hashval = hash;
		ip = addr;
		port = portNum;
		socket = aSocket;
		in = null;
		routingTable = rTable;
		
		queue = aQueue;
		
	}
	
	public void setHash(String passedHash){
		
		//part for election
		isElecting[0] = true;
		System.out.println("election started!!");
		hashval.replace(0, hashval.length(), passedHash);
		isElecting[0] = true;
		hasLeader[0] = false;
		Election el = new Election(routingTable, hashval, isElecting, hasLeader, queue);
		el.start();
			
		
	}
	
	
	public void updateTable(RoutingRecord record){
		
		synchronized(this.routingTable){		
			for(RoutingRecord rr : routingTable){
				
				if(rr.IP.equals(record.IP) && rr.port == record.port){
					rr.ID = record.ID;
				}
				
			}
		}
		
	}
	
	
	
	public void updateQueue (QueueRecord[] recQueue){
		
		boolean isEmpty = true;
		
		for(int i = 0; i < recQueue.length; i++){
			if(recQueue[i] != null){
				isEmpty = false;
			}
		}
		
		if(isEmpty){
			System.out.println("a key has been found!! I'm free!!!");
			hasLeader[0] = false;
			isElecting[0] = false;
		}
		
		this.queue = recQueue;
	}
	
	
	public void checkRange(String toFind, String check, String pref, int first, int last, int noOfVar) throws IOException{
		
		String[] result = StringChecker.compute(toFind, check, pref,first, last, noOfVar);
		String[] sendBack = new String[4];
		sendBack[0] = result[0];
		sendBack[1] = result[1];
		
		Socket leaderSocket = null;
		for(RoutingRecord rr : routingTable){
			if(rr.isLeader)
				leaderSocket = rr.socket;
			if(rr.isMe){
				sendBack[2] = rr.IP;
				sendBack[3] = Integer.toString(rr.port);
			}
				
		}
		//sending computation results back to leader
		new ObjectOutputStream(leaderSocket.getOutputStream()).writeObject(sendBack);
		System.out.println("answered to the leader for " + toFind);
		
	}
	
	
	public void run(){		
		
//		System.out.printf("InputReceiver: ready to receive data from: %s - %d\n", socket.getInetAddress(),  socket.getPort());
		
		try { 
			
			while(true){
				
				in = new ObjectInputStream(socket.getInputStream());
		    
				Object o = in.readObject();
//				System.out.println(" i received a " + o.getClass().toString());
				//if the received object is a string
				if(o.getClass().equals(String.class)){
					
					setHash((String)o);
				
				//if the received object is a routing record
				} else if (o.getClass().equals(RoutingRecord.class)){
//					System.out.printf("%d - %d\n",((RoutingRecord)o).port, ((RoutingRecord)o).ID);
					updateTable((RoutingRecord)o);
				} else if (o.getClass().equals(SendingStrings.class)){
//					System.out.printf("received a sendingString\n");
					SendingStrings ss = (SendingStrings) o;
					checkRange(ss.hash,ss.checkingHash,ss.prefix,ss.firstChar,ss.lastChar,ss.freeChars);
				} else if(o.getClass().equals(String[].class)){
//					System.out.printf("received a solution\n");
					Leader.checkSolution((String[])o, routingTable, queue, hasLeader, hashval);
				} else if(o.getClass().equals(QueueRecord[].class)){
					System.out.printf("received a complete queue\n");
					this.updateQueue((QueueRecord[])o);
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
				
				new Election(routingTable, hashval, isElecting, hasLeader, queue).start();				
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
