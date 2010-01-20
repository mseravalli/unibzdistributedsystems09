package node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cracker.StackRecord;



public class ConnectionsReceiver implements Runnable {
	
	private boolean[] isElecting;
	private boolean[] hasLeader;

	private StringBuffer hashval;
	private int port;
	private ServerSocket listenSocket;
	private Socket incomingSocket;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private ArrayList <RoutingRecord> routingTable;
	
	private StackRecord[] stack;
	
	
	/**
	 * 
	 * @param ipAddress
	 * @param portAddress
	 * @param hashval 
	 */
	public ConnectionsReceiver(int portAddress, ArrayList <RoutingRecord> rTable, boolean[] electing, boolean[] working, StringBuffer hash, StackRecord[] aStack){
		
		isElecting = electing;
		hasLeader = working;
		
		hashval = hash;
		this.port = portAddress;
		this.listenSocket = null;
		this.incomingSocket = null;
		
		this.out = null;
		this.in = null;
		
		this.routingTable = rTable;
		
		stack = aStack;
	}
	
	
	public static ArrayList <RoutingRecord> copyRoutingTable(ArrayList <RoutingRecord> rTable){

		ArrayList <RoutingRecord> newRT  = new ArrayList <RoutingRecord>();
		
		for(RoutingRecord rr : rTable){
			newRT.add(new RoutingRecord(rr.IP, rr.port, RoutingRecord.IS_NOT_ME, rr.ID));
		}
		
		return newRT;
		
	}
	
	
	@Override
	public void run() {
		try {
			listenSocket = new ServerSocket(port);
			
			while(true){
				
//				System.out.println("Listening on " + port);			
				
				incomingSocket = listenSocket.accept();
				
				in = new ObjectInputStream(incomingSocket.getInputStream());
				out = new ObjectOutputStream(incomingSocket.getOutputStream());
				
				HelloPacket packet = (HelloPacket) in.readObject();
				routingTable.add(new RoutingRecord(packet.IP, packet.port, RoutingRecord.IS_NOT_ME,Node.NULL_ID,incomingSocket));
				
				ArrayList <RoutingRecord> sendTable = copyRoutingTable(this.routingTable);				
							
				out.writeObject(sendTable);
				out.flush();
				
				//prints the routing table
//				for(RoutingRecord rr : routingTable){
//					if(rr.socket != null)
//						System.out.printf("%s:%d %b %s\n", rr.IP, rr.port, rr.isMe, rr.socket.toString());
//					else
//						System.out.printf("%s:%d %b %o\n", rr.IP, rr.port, rr.isMe, rr.socket);
//				}
				
				InputReceiver receiver = new InputReceiver(packet.IP, packet.port, incomingSocket, routingTable, isElecting, hasLeader, hashval, stack);
				receiver.start();
								
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
