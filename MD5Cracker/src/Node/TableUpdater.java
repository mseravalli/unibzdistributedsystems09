package Node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import cracker.RoutingRecord;

public class TableUpdater extends Thread{
	
//	private Socket socket;
	private ObjectInputStream in;
	private HelloPacket packet;
	private ArrayList <RoutingRecord> routingTable;
	
	public TableUpdater(ArrayList <RoutingRecord> rTable, ObjectInputStream inStream){
		this.routingTable = rTable;
		in = inStream;
//		this.socket = aSocket;
		this.packet = null;
	}
	
	
	/**
	 * receives the hello packages
	 */
	public void run(){
		
		try {
//			in = new ObjectInputStream(this.socket.getInputStream());
			
			packet = (HelloPacket) in.readObject();
			
			if(!packet.isHello){
				updateRoutingTable(packet);
			}
			
			System.out.println("Table updater: This is the routing table");
			for(int i = 0; i< routingTable.size();i++){
				System.out.printf("%s:%d\n",routingTable.get(i).IP, routingTable.get(i).port);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void updateRoutingTable(HelloPacket node) {
		
		boolean isAlreadyThere = false;
		for(int i = 0;i<routingTable.size();i++){
			if((routingTable.get(i).IP.equals(node.IP)) && (routingTable.get(i).ID == node.ID))
				isAlreadyThere = true;
			
		}
		
		if (!isAlreadyThere)
			routingTable.add(new RoutingRecord(node.IP, node.port, false));
		
	}
	

}
