package Node;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

import cracker.Election;
import cracker.RoutingRecord;

public class InitialConnection implements Runnable{
	
	private static final String INTERFACE_NAME = "wlan0";

	private static final boolean IS_ME = true;
	
	private ArrayList <RoutingRecord> routingTable;
	private Socket connectionSocket;
	private Socket mySocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String IP;
	private String isNew;
	private int port;
	private Ping ping;
	private Node node;
	private Receiever rec;
	private TableUpdate update;
	private Election el;
	
	
	public InitialConnection(String connectionIP, int pt){
		isNew = connectionIP;
		port = pt;
		IP = getOwnIP(INTERFACE_NAME);	
		routingTable = new ArrayList <RoutingRecord>();
	}
	
	public void newNetwork(){
		
		routingTable.add(new RoutingRecord(getOwnIP(INTERFACE_NAME),port,IS_ME));
		
		System.out.println("Creating new Network");
		
		//start Receiver, which is waiting for HelloPackets
		rec = new Receiever(routingTable,port);
		rec.start();
		
		System.out.println("Receiever started");
		
		//start Ping, which sends out acknowledgments every second
		ping = new Ping(routingTable);
		ping.start();
		
		
		Scanner sc = new Scanner(System.in);
		sc.next();
		//elect Leader
		el = new Election(routingTable,IP,port);
		
	}
	
	public void connectToNode(String connectionIP){
		
		String IP = connectionIP.substring(0, connectionIP.indexOf(':'));
		Integer Port = Integer.parseInt(connectionIP.substring(connectionIP.indexOf(':')+1));
		
		System.out.println("trying to connect to " + connectionIP);
		
		try {
			
			connectionSocket = new Socket(IP, Port.intValue());
			
			System.out.println("connected to : " + IP + ":" +Port.toString());
			
			out = new ObjectOutputStream(connectionSocket.getOutputStream());
			//arriva solo fino qua e non piu avanti	
			
			
			in = new ObjectInputStream(this.connectionSocket.getInputStream());
			
			
			System.out.println("STREAMS CREATED, READY TO WRITE");
			HelloPacket hp = new HelloPacket(getOwnIP(INTERFACE_NAME), port , true, RoutingRecord.NULL_ID);
			
			out.writeObject(hp);
			out.flush();
			System.out.println("Object sent");
			
//			routingTable = (ArrayList <RoutingRecord>) in.readObject();
			in.readObject();
			routingTable.add(new RoutingRecord(getOwnIP(INTERFACE_NAME),port,IS_ME));
			routingTable.add(new RoutingRecord(IP,Port,false, -1, connectionSocket));
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//start Receiver, which is waiting for HelloPackets
		rec = new Receiever(routingTable,port);
		rec.start();
		
		System.out.println("Receiever started");
		
		//start Ping, which sends out acknowledgments every second
		ping = new Ping(routingTable);
		ping.start();
		
		System.out.println("Ping started");
		
		//start Tableupdate, which checks for nodes that left connection
		update = new TableUpdate(routingTable);
		update.start();
		
		Scanner sc = new Scanner(System.in);
		sc.next();
		
		//elect Leader
		el = new Election(routingTable,IP,port);
		
		
	}	
		
	//Returns the correct network ip-address, given an InterfaceName
	public String getOwnIP(String interfaceName){
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

	@Override
	public void run() {
		if(this.isNew.equals("new"))
			newNetwork();
	else
		connectToNode(this.isNew);
		
	}
	
	
	public static void main (String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("first insert the ip and then the port to use");
		
		Runnable first = new InitialConnection(sc.next(),sc.nextInt());		
		Thread firstThread = new Thread(first);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		firstThread.start();		
		
	}
	
	
}
