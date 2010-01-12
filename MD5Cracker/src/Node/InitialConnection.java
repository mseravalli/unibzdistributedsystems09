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

import cracker.RoutingRecord;

public class InitialConnection {
	
	private static final String INTERFACE_NAME = "wlan0";
	private static final int NO_ID = -1;
	
	private ArrayList <RoutingRecord> routingTable;
	private Socket connectionSocket;
	private Socket mySocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int port;
	private Ping ping;
	private Node node;
	private Reciever rec;
	
	
	public InitialConnection(String connectionIP,int pt){
		port = pt;
		if(connectionIP.equals("new"))
				newNetwork();
		else
			connectToNode(connectionIP);
		
		
	}
	
	public void newNetwork(){
		routingTable = new ArrayList <RoutingRecord>();
		
		//start Reciever, which is waiting for HelloPackets
		rec = new Reciever(routingTable,port);
		rec.start();
		
		//start Ping, which 
		ping = new Ping(routingTable);
		ping.start();
		
	}
	
	public void connectToNode(String connectionIP){
		
		String IP = connectionIP.substring(0, connectionIP.indexOf(':'));
		Integer Port = Integer.parseInt(connectionIP.substring(connectionIP.indexOf(':')+1));
		
		try {
			
			connectionSocket = new Socket(IP, Port.intValue());
			out = new ObjectOutputStream(connectionSocket.getOutputStream());
			in = new ObjectInputStream(connectionSocket.getInputStream());
			
			out.writeObject(new HelloPacket(getOwnIP(INTERFACE_NAME),port,true,NO_ID));
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
	
	//public void createNode(){
	//	
		
	//}
	
	
	
		
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
