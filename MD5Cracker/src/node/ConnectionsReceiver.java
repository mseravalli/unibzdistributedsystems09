package node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Node.HelloPacket;

import cracker.RoutingRecord;

public class ConnectionsReceiver implements Runnable {

	private String ip;
	private int port;
	private ServerSocket listenSocket;
	private Socket incomingSocket;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private ArrayList <RoutingRecord> routingTable;
	
	
	/**
	 * 
	 * @param ipAddress
	 * @param portAddress
	 */
	public ConnectionsReceiver(String ipAddress, int portAddress, ArrayList <RoutingRecord> rTable){
		this.ip = ipAddress;
		this.port = portAddress;
		this.listenSocket = null;
		this.incomingSocket = null;
		
		this.out = null;
		this.in = null;
		
		this.routingTable = rTable;
	}
	
	
	@Override
	public void run() {
		try {
			listenSocket = new ServerSocket(port);
			
			while(true){
				
				System.out.println("Listening");
				
				incomingSocket = listenSocket.accept();
				
				in = new ObjectInputStream(incomingSocket.getInputStream());
				out = new ObjectOutputStream(incomingSocket.getOutputStream());
				
				System.out.println("connection accepted");
				
				//receives a hello packet from the new node and adds it to the table
				HelloPacket packet;
				try {
					packet = (HelloPacket) in.readObject();
					routingTable.add(new RoutingRecord(packet.IP, packet.port, RoutingRecord.IS_NOT_ME));
					
					//TODO maybe we can send the current routing table to the connected node
					
					
					InputReceiver receiver = new InputReceiver(in);
					receiver.start();
					
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
