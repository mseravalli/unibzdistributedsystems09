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
				
				System.out.println("Listening on " + port);			
				
				incomingSocket = listenSocket.accept();
				
				in = new ObjectInputStream(incomingSocket.getInputStream());
				out = new ObjectOutputStream(incomingSocket.getOutputStream());
				
				HelloPacket packet = (HelloPacket) in.readObject();
				routingTable.add(new RoutingRecord(packet.IP, packet.port, RoutingRecord.IS_NOT_ME,Node.NULL_ID,incomingSocket));
				
				ArrayList <RoutingRecord> sendTable = new ArrayList <RoutingRecord>();
				
				for(RoutingRecord rr : this.routingTable){
					sendTable.add(rr);
				}
				
				for(RoutingRecord st : sendTable){
					st.socket=null;
				}
							
				out.writeObject(sendTable);
				out.flush();
				
				System.out.println("packet received and table updated");
				for(RoutingRecord rr : routingTable)
					System.out.printf("%s:%d %b\n", rr.IP, rr.port, rr.isMe);
				
				InputReceiver receiver = new InputReceiver(in, routingTable);
				receiver.start();
								
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
