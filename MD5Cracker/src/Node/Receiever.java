package Node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import cracker.RoutingRecord;

public class Receiever extends Thread{
	
	private static final boolean IS_NOT_ME = false;
	private ArrayList <RoutingRecord> routingTable;
	private Socket nodeSocket;
	private int port;
	
	public Receiever(ArrayList <RoutingRecord> rT, int p){
		routingTable = rT;
		port = p;
	}
	
	public void run(){			
		ServerSocket listenSocket = null;
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket nodeSocket;
		ObjectInputStream in;
		ObjectOutputStream out;
		HelloPacket packet;
	    
		while(true){
			
			try {
			
			System.out.println("listening");
			
			nodeSocket = listenSocket.accept();
				
			System.out.println("new connection accepted");
				
			in = new ObjectInputStream(nodeSocket.getInputStream());
			out = new ObjectOutputStream(nodeSocket.getOutputStream());
				
//			in.readObject();				
//			System.out.println("the packet is arrived from: ");
			packet = (HelloPacket) in.readObject();
				
			System.out.println("the received packet is correct");
				
			if(packet.hello){
				sendRoutingTable();
			}
			else{
				updateRoutingTable(packet);
			}
			nodeSocket.close();
				
			for(int i = 0; i< routingTable.size();i++){
				System.out.println(routingTable.get(i).IP);
			}
				
			Thread.sleep(500);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
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
		
		boolean isAlreadyThere = false;
		for(int i = 0;i<routingTable.size();i++){
			if((routingTable.get(i).IP.equals(node.IP)) && (routingTable.get(i).ID == node.ID))
				isAlreadyThere = true;
			
		}
		
		if (!isAlreadyThere)
			routingTable.add(new RoutingRecord(node.IP,node.port,node.ID,IS_NOT_ME));
		
	}

}


