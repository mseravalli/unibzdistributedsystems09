package node;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

import cracker.QueueRecord;
import cracker.StringChecker;



public class Node {
	
	public static final int NULL_ID = -1;
	
	private boolean[] isElecting;
	private boolean[] hasLeader;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private Socket mySocket;
	private String myIP;
	private int myPort;
	private String connectionIP;
	
	private StringBuffer hashval;
		
	private ArrayList <RoutingRecord> routingTable;
	
	private QueueRecord[] queue;


	public Node(String ipAddress, int portAddress){
		
		isElecting = new boolean[1];
		isElecting[0] = false;
		hasLeader = new boolean[1];
		hasLeader[0] = false;
		
		
		myIP = Node.getOwnIP();
		myPort = portAddress;
		
		connectionIP = ipAddress;
		
		out = null;
		in = null;
		
		hashval = new StringBuffer();
		
		routingTable = new ArrayList <RoutingRecord>();
		
		routingTable.add(new RoutingRecord(myIP, myPort, RoutingRecord.IS_ME));
		
		queue = new QueueRecord[64];
		
	}
		
	/**
	 * The method returns the ip of either wlan0 or eth0
	 * 
	 * @return
	 */
	public static String getOwnIP(){
		
		String interfaceName = "";
		
    	String ip = "";
    	NetworkInterface net = null;
		try {
			interfaceName = "eth0";
			net = NetworkInterface.getByName(interfaceName);
			if(net == null){
				interfaceName = "wlan0";
				net = NetworkInterface.getByName(interfaceName);
			}
				
		} catch (SocketException e) {
			System.out.println("no IP for " + interfaceName);
			e.printStackTrace();
		}
		if(net != null){
	    	Enumeration<InetAddress> ipadds = net.getInetAddresses();
	        while(ipadds.hasMoreElements()){
	        	ip = ipadds.nextElement().toString().substring(1);
	        }
		} else {
			System.out.println("no IP for the passed interface retry");
			System.exit(0);
		}
        return ip;
    }
	
	/**
	 * 
	 * The method receives a routing table and clears the socket field and set
	 * as true the field isMe only for the record that actually has node's ip
	 * 
	 * @param receivedTable
	 * @return
	 */
	public ArrayList <RoutingRecord> cleanTable(ArrayList <RoutingRecord> receivedTable){
		
		for(RoutingRecord rr : receivedTable){
			
			if(rr.IP.equals(myIP) && rr.port == myPort){
				rr.isMe = true;
			} else {
				rr.isMe = false;
			}
			
			rr.socket = null;
			
		}
		
		return receivedTable;
		
	}
	
	/**
	 * the method select the nodes that are not in the routing table and adds them
	 * into it if one of the nodes is the leader the global variable hasLeader 
	 * will be set to true
	 * 
	 * @param newRoutingTable
	 */
	public void addNewRecords(ArrayList <RoutingRecord> newRoutingTable){
		
		for(RoutingRecord newRecord : newRoutingTable){
		
			if(newRecord.isLeader){
				hasLeader[0] = true;
			}
			
			boolean isPresent = false;
			
			for(RoutingRecord rr: routingTable){
				if(newRecord.IP.equals(rr.IP) && (newRecord.port == rr.port)){
					isPresent = true;
					//this may be not necessary
					rr.isLeader = newRecord.isLeader;
				}
			}
			if(!isPresent){
				routingTable.add(newRecord);
			}
		}
	}

	public void connectToNode(String ipAddress, int portAddress){		
		
		try {
			
			//new node to the passed address is created
			mySocket = new Socket(ipAddress, portAddress);
			
			out = new ObjectOutputStream(mySocket.getOutputStream());
			in = new ObjectInputStream(mySocket.getInputStream());
			
			// a HelloPacket is created and sent to the node
			HelloPacket packet = new HelloPacket(this.myIP, this.myPort, true, NULL_ID);
			
			out.writeObject(packet);
			out.flush();
			
			/*
			 * check whether the record is already presennt because the socket must
			 * be added to the routing table, if the node is not present a new record
			 * will be created from scratch, otherwise the socket will be added to
			 * the existing one 	
			 */
			boolean isPresent = false;
			int position = -1;
			for(int i = 0; i < routingTable.size(); i++){
				RoutingRecord rr = routingTable.get(i);
				if(rr.IP.equals(ipAddress) && rr.port == portAddress){
					isPresent = true;
					position = i;
				}
			}
			
			if(isPresent){
				routingTable.get(position).socket = mySocket;
			}else {
				this.routingTable.add(new RoutingRecord(ipAddress, portAddress, RoutingRecord.IS_NOT_ME, Node.NULL_ID, mySocket));
			}
			
			
			//the other object will return its routing table
			ArrayList <RoutingRecord> readObject = (ArrayList <RoutingRecord>) in.readObject();
			
			this.addNewRecords(cleanTable(readObject));
			
			//An input receiver will be started and will listen from the connected node 
			new InputReceiver(ipAddress,portAddress,mySocket,routingTable,isElecting, hasLeader, hashval, queue).start();
			
//			System.out.println("Node: connected to " + portAddress);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startNode(){
		
		// if the current node it not the fisrt one it will try to connect to another one
		if(!this.connectionIP.equals("new")){
			
			//the string is divided into ip and port
			String conIP = connectionIP.substring(0, connectionIP.indexOf(':'));
			int conPort = Integer.parseInt(connectionIP.substring(connectionIP.indexOf(':')+1));
			
			this.connectToNode(conIP, conPort);
			
			//the node will connect to every node present in the list
			for(int i = 0; i < routingTable.size(); i++){
				RoutingRecord rr = routingTable.get(i);
				if((!rr.isMe) && (rr.socket == null)){
					this.connectToNode(rr.IP,rr.port);
				}
			}
			
		}		
		
		//the node is ready to receive connections from other nodes
		Runnable runnable = new ConnectionsReceiver(this.myPort, this.routingTable, isElecting, hasLeader, hashval, queue);
		Thread thread = new Thread(runnable); 
		thread.start();
		
	}
	
	
	public void startElection(String hash){
		
		if(!isElecting[0] && !hasLeader[0]){
		
			hashval.replace(0, hashval.length(), hash);
			isElecting[0] = true;
			try {
				for(RoutingRecord rr : routingTable){
					if(!rr.isMe){
						out = new ObjectOutputStream( rr.socket.getOutputStream());
						out.writeObject(hashval.toString());
						out.flush();
					}			
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			isElecting[0] = true;
			hasLeader[0] = false;
			Election el = new Election(routingTable, hashval, isElecting, hasLeader, queue);
			el.start();
			
			System.out.println("Election started");
		
		} else if (isElecting[0]){
			System.out.println("An election is currently taking place");
		} else if (hasLeader[0]){
			System.out.println("There is already a leader and I am computing some work");
		}
		
	}
	
	
	
	public static void broadcastObject(ArrayList<RoutingRecord> rTable, Object toSend){		
		
		//the method sends the passed object to all the connected nodes
		
		ObjectOutputStream out;
		
		try {
			for(RoutingRecord rr : rTable){
				if(!rr.isMe){
					out = new ObjectOutputStream(rr.socket.getOutputStream());
					out.writeObject(toSend);
					out.flush();
				}			
			}
		} catch (SocketException e) {
			System.out.printf("Node: node disconnected\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		System.out.println("insert ip to connect address and port");
		String address = sc.next();
		int port = sc.nextInt();

		Node node = new Node(address, port);		
		
		node.startNode();
		
		String inputString = "exit";
		//Ciao == 16272a5dd83c63010e9f67977940e871
		do{
			System.out.println("insert the hash to decode");
			inputString = sc.next();
			
			if(inputString.equals("exit")){
				System.exit(0);
			}
			
			inputString = StringChecker.encode(inputString);
			
			node.startElection(inputString);
		}while(true);
		
	}
	

}
