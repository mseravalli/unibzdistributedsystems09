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

import Node.HelloPacket;

import cracker.RoutingRecord;

public class Node {
	
	public static final int NULL_ID = -1;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket mySocket;
	private String myIP;
	private int myPort;
	private String connectionIP;
	private ArrayList <RoutingRecord> routingTable;

	public Node(String ipAddress, int portAddress){
		
		this.myIP = Node.getOwnIP("wlan0");
		this.myPort = portAddress;
		
		this.connectionIP = ipAddress;
		
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
	
	
	public void connectToNode(String ipAddress, int portAddress){		
		
		try {
			
			mySocket = new Socket(ipAddress, portAddress);
			
			out = new ObjectOutputStream(mySocket.getOutputStream());
			in = new ObjectInputStream(mySocket.getInputStream());
			
			System.out.println("connected to " + this.connectionIP);
			
			HelloPacket packet = new HelloPacket(this.myIP, this.myPort, true, NULL_ID);
			
			out.writeObject(packet);
			out.flush();
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void startNode(){
		
		if(!this.connectionIP.equals("new")){
			
			String conIP = connectionIP.substring(0, connectionIP.indexOf(':'));
			int conPort = Integer.parseInt(connectionIP.substring(connectionIP.indexOf(':')+1));
			
			this.connectToNode(conIP, conPort);
		}
		
		Runnable runnable = new ConnectionsReceiver(this.myIP, this.myPort, this.routingTable);
		Thread thread = new Thread(runnable); 
		thread.start();
		
	}
	
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("insert ip to connect address and port");
		String address = sc.next();
		int port = sc.nextInt();

		Node node = new Node(address, port);		
		
		node.startNode();
		
	}
	

}
