package Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class InitialConnection {
	
	private static final String INTERFACE_NAME = "wlan0";
	
	private ArrayList <RoutingRecord> routingTable;
	private Socket connectionSocket;
	private Socket mySocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String IP;
	private int port;
	private Ping ping;
	private Node node;
	private Receiever rec;
	private Election el;
	
	
	public InitialConnection(String connectionIP,int pt){
		port = pt;
		IP = getOwnIP(INTERFACE_NAME);
		
		if(connectionIP.equals("new"))
				newNetwork();
		else
			connectToNode(connectionIP);
		
		
	}
	
	public void newNetwork(){
		routingTable = new ArrayList <RoutingRecord>();
		
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
		
		try {
			
			connectionSocket = new Socket(IP, Port.intValue());
			out = new ObjectOutputStream(connectionSocket.getOutputStream());
			in = new ObjectInputStream(connectionSocket.getInputStream());
			
			out.writeObject(new HelloPacket(getOwnIP(INTERFACE_NAME),port,true,-1));
			routingTable = (ArrayList <RoutingRecord>) in.readObject();
			
			//routingTable.add(new RoutingRecord(this.getOwnIP("wlan0"),port));
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
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
	
	
}
