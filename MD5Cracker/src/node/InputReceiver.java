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
	
	private StringBuffer hashval;
	private String ip;
	private int port;
	
	private Socket socket;
	
	private ObjectInputStream in;
//	private ArrayList <RoutingRecord> routingTable;
	
	private QueueRecord[] queue;
	
	public InputReceiver(String addr, int portNum, Socket aSocket, StringBuffer hash, QueueRecord[] aQueue){
		
		hashval = hash;
		ip = addr;
		port = portNum;
		socket = aSocket;
		in = null;
//		routingTable = rTable;
		
		queue = aQueue;
		
	}
	
	public void setHash(String passedHash){
		
		//part for election
//		Node.setIsElecting(true);
		Node.setIsElecting(true);
		System.out.println("election started!!");
		hashval.replace(0, hashval.length(), passedHash);
//		Node.hasLeader = false;
		Node.setHasLeader(false);
		Election el = new Election(hashval);
		el.start();
			
		
	}
	
	
	public void updateTable(RoutingRecord record){
		
		synchronized(Node.getRoutingTable()){		
			for(RoutingRecord rr : Node.getRoutingTable()){
				
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
			System.out.println("Work finished\nInsert the hash to decode");
			Node.setHasLeader(false);
			Node.setIsElecting(false);
			
			for(RoutingRecord rr : Node.getRoutingTable()){
				rr.isLeader = false;
			}
			
		}
		
		this.queue = recQueue;
	}
	
	
	public void checkRange(String toFind, String check, String pref, int first, int last, int noOfVar) throws IOException{
		
		String[] result = StringChecker.compute(toFind, check, pref, first, last, noOfVar);
		String[] sendBack = new String[4];
		sendBack[0] = result[0];
		sendBack[1] = result[1];
		
		Socket leaderSocket = null;
		for(RoutingRecord rr : Node.getRoutingTable()){
			if(rr.isLeader)
				leaderSocket = rr.socket;
			if(rr.isMe){
				sendBack[2] = rr.IP;
				sendBack[3] = Integer.toString(rr.port);
			}
				
		}
		//sending computation results back to leader
		new ObjectOutputStream(leaderSocket.getOutputStream()).writeObject(sendBack);
		System.out.println("Answered to the leader for " + pref);
		
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
					System.out.printf("received a solution\n");
					Leader.checkSolution((String[])o, queue, hashval);
				} else if(o.getClass().equals(QueueRecord[].class)){
					this.updateQueue((QueueRecord[])o);
				}
				
		    	
			}
			
		    
		} catch(EOFException e) {
                    
			System.out.printf("node %s:%d disconnected because of a EOFException\n", ip, port);
			
			updateRoutingTable();
			
		} catch (SocketException e) {
			System.out.printf("node %s:%d disconnected because of a SocketException\n", ip, port);
			
			updateRoutingTable();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	private void updateRoutingTable(){
		int position = -1;
			
			for(int i = 0; i < Node.getRoutingTable().size(); i++){
				if(Node.getRoutingTable().get(i).IP.equals(ip) && Node.getRoutingTable().get(i).port == port){
					position = i;
				}
					
			}
			
			if(Node.getRoutingTable().get(position).isLeader){
				Node.getRoutingTable().remove(position);
				
				new Election(hashval).start();				
			} else {
				Node.getRoutingTable().remove(position);
			}
	}
}
