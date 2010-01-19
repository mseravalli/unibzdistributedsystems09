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



public class Node {
	
	public static final int NULL_ID = -1;
	
	public static final String ELECTION = "election";
	
	private boolean[] isElecting;
	private boolean[] isWorking;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket mySocket;
	private String myIP;
	private StringBuffer hashval;
	private int myPort;
	private String connectionIP;
	private ArrayList <RoutingRecord> routingTable;
	


	public Node(String ipAddress, int portAddress){
		
		isElecting = new boolean[1];
		isElecting[0] = false;
		isWorking = new boolean[1];
		isWorking[0] = false;
		
		
		myIP = Node.getOwnIP("wlan0");
		myPort = portAddress;
		
		connectionIP = ipAddress;
		
		out = null;
		in = null;
		
		routingTable = new ArrayList <RoutingRecord>();
		
		routingTable.add(new RoutingRecord(myIP, myPort, RoutingRecord.IS_ME));
		
	}
	
	public ArrayList <RoutingRecord> getRoutingTable(){
		return this.routingTable;
	}
	
	public static String getOwnIP(String interfaceName){
    	String ip = "";
    	NetworkInterface net = null;
		try {
			net = NetworkInterface.getByName(interfaceName);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Enumeration<InetAddress> ipadds = net.getInetAddresses();
        while(ipadds.hasMoreElements()){
        	ip = ipadds.nextElement().toString().substring(1);
        }
        return ip;
    }
	
	
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
	
	
	public void connectToNode(String ipAddress, int portAddress){		
		
		try {
			
			mySocket = new Socket(ipAddress, portAddress);
			
			out = new ObjectOutputStream(mySocket.getOutputStream());
			in = new ObjectInputStream(mySocket.getInputStream());
			
			
			
			HelloPacket packet = new HelloPacket(this.myIP, this.myPort, true, NULL_ID);
			
			out.writeObject(packet);
			out.flush();
			
			ArrayList <RoutingRecord> readObject = (ArrayList <RoutingRecord>) in.readObject();
			
			//check whether the record is already present and modifies it		
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
			}else
				this.routingTable.add(new RoutingRecord(ipAddress, portAddress, RoutingRecord.IS_NOT_ME, Node.NULL_ID, mySocket));
			
			
			
			this.addNewRecords(cleanTable(readObject));
			
			//prints the routing table
//			for(RoutingRecord rr : routingTable){
//				if(rr.socket != null)
//					System.out.printf("%s:%d %b %s\n", rr.IP, rr.port, rr.isMe, rr.socket.toString());
//				else
//					System.out.printf("%s:%d %b %o\n", rr.IP, rr.port, rr.isMe, rr.socket);
//			}
			
			new InputReceiver(ipAddress,portAddress,mySocket,routingTable,isElecting, isWorking, hashval).start();
			
//			System.out.println("Node: connected to " + portAddress);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addNewRecords(ArrayList <RoutingRecord> newRoutingTable){
		for(RoutingRecord nr : newRoutingTable){
			boolean isPresent = false;
			for(RoutingRecord rr: routingTable){
				if(nr.IP.equals(rr.IP) && (nr.port == rr.port))
					isPresent = true;
			}
			if(!isPresent){
				routingTable.add(nr);
			}
		}
	}
	
	
	public void startNode(){
		
		if(!this.connectionIP.equals("new")){
			
			String conIP = connectionIP.substring(0, connectionIP.indexOf(':'));
			int conPort = Integer.parseInt(connectionIP.substring(connectionIP.indexOf(':')+1));
			
			this.connectToNode(conIP, conPort);
			
			for(int i = 0; i < routingTable.size(); i++){
				RoutingRecord rr = routingTable.get(i);
				if((!rr.isMe) && (rr.socket == null)){
					this.connectToNode(rr.IP,rr.port);
				}
			}
			
		}		
		
		Runnable runnable = new ConnectionsReceiver(this.myIP, this.myPort, this.routingTable, isElecting, isWorking, hashval);
		Thread thread = new Thread(runnable); 
		thread.start();
		
	}
	
	
	public void startElection(String hash){
		hashval = new StringBuffer(hash);
		isElecting[0] = true;
		try {
			for(RoutingRecord rr : routingTable){
				if(!rr.isMe){
					out = new ObjectOutputStream( rr.socket.getOutputStream());
					out.writeObject(this.ELECTION + hashval.toString());
					out.flush();
				}			
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Election el = new Election(routingTable,hashval);
		el.start();
		
		System.out.println("Election started");
		
	}
	
	
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		System.out.println("insert ip to connect address and port");
		String address = sc.next();
		int port = sc.nextInt();

		Node node = new Node(address, port);		
		
		node.startNode();
		
		System.out.println("insert the hash to decode");
		//TODO check whether isWorking
		node.startElection("hashval:"+sc.next());
		
		
	}
	

}
