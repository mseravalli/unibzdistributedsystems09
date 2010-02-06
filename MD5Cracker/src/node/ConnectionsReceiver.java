package node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import cracker.QueueRecord;



public class ConnectionsReceiver implements Runnable {
	
	private StringBuffer hashval;
	private int port;
	private ServerSocket listenSocket;
	private Socket incomingSocket;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	
	/**
	 * 
	 * @param ipAddress
	 * @param portAddress
	 * @param hashval 
	 */
	public ConnectionsReceiver(int portAddress, StringBuffer hash){
		
		hashval = hash;
		this.port = portAddress;
		this.listenSocket = null;
		this.incomingSocket = null;
		
		this.out = null;
		this.in = null;
		
	}
	
	
	public static ArrayList <RoutingRecord> copyRoutingTable(ArrayList <RoutingRecord> rTable){

		ArrayList <RoutingRecord> newRT  = new ArrayList <RoutingRecord>();
		
		
		for(RoutingRecord rr : rTable){
			newRT.add(new RoutingRecord(rr.IP, rr.port, RoutingRecord.IS_NOT_ME, rr.ID, null, rr.isLeader));
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
				Node.getRoutingTable().add(new RoutingRecord(packet.IP, packet.port, RoutingRecord.IS_NOT_ME,Node.NULL_ID,incomingSocket));
				
				ArrayList <RoutingRecord> sendTable = copyRoutingTable(Node.getRoutingTable());				
							
				out.writeObject(sendTable);
				out.flush();
				
				//prints the routing table
//				for(RoutingRecord rr : routingTable){
//					if(rr.socket != null)
//						System.out.printf("%s:%d %b %s\n", rr.IP, rr.port, rr.isMe, rr.socket.toString());
//					else
//						System.out.printf("%s:%d %b %o\n", rr.IP, rr.port, rr.isMe, rr.socket);
//				}
				
				InputReceiver receiver = new InputReceiver(packet.IP, packet.port, incomingSocket, hashval);
				receiver.start();
								
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
