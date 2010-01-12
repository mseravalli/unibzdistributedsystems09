package Node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import cracker.RoutingRecord;

public class Receiever extends Thread{
	
	private ArrayList <RoutingRecord> routingTable;
	private Socket nodeSocket;
	private int port;
	
	public Receiever(ArrayList <RoutingRecord> rT, int p){
		routingTable = rT;
		port = p;
	}
	
	public void run(){
		try {
			
			ServerSocket listenSocket;
			listenSocket = new ServerSocket(port);
			Socket nodeSocket;
			ObjectInputStream in;
			HelloPacket packet;
	    
			while(true){
				nodeSocket = listenSocket.accept();
				in = new ObjectInputStream(nodeSocket.getInputStream());
				packet = (HelloPacket) in.readObject();
				if(packet.hello){
					sendRoutingTable();
				}
				else{
					updateRoutingTable(packet);
				}
				nodeSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendRoutingTable() {

		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(nodeSocket.getOutputStream());
			out.writeObject(routingTable);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateRoutingTable(HelloPacket node) {
		routingTable.add(new RoutingRecord(node.IP,node.port));
		
	}

}


