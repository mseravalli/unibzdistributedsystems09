//package Node;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//
//import node.HelloPacket;
//import node.RoutingRecord;
//
//public class Receiever extends Thread{
//	
//	private static final boolean IS_NOT_ME = false;
//	private ArrayList <RoutingRecord> routingTable;
//	private Socket nodeSocket;
//	private ObjectInputStream in;
//	private ObjectOutputStream out;
//	private int port;
//	
//	public Receiever(ArrayList <RoutingRecord> rT, int p){
//		routingTable = rT;
//		port = p;
//	}
//	
//	public void run(){			
//		ServerSocket listenSocket = null;
//		try {
//			listenSocket = new ServerSocket(port);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		HelloPacket packet;
//	    
//		while(true){
//			
//			try {
//			
//			System.out.println("listening");
//			
//			nodeSocket = listenSocket.accept();
//				
//			System.out.println("new connection accepted");
//				
//			in = new ObjectInputStream(nodeSocket.getInputStream());
//			out = new ObjectOutputStream(nodeSocket.getOutputStream());
//				
//
//			packet = (HelloPacket) in.readObject();
//				
//			System.out.println("the received packet is correct");
//				
//			if(packet.isHello){
//				sendRoutingTable();
//				updateRoutingTable(packet);
//			}
//			else{
//				updateRoutingTable(packet);
//			}
//			
//						
//			TableUpdater tu = new TableUpdater(routingTable, nodeSocket);
//			tu.start();
//			
//			System.out.println("This is the received routing table");
//			for(int i = 0; i< routingTable.size();i++){
//				System.out.printf("%s:%d\n",routingTable.get(i).IP, routingTable.get(i).port);
//			}
//				
//			Thread.sleep(2000);
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//				
//		}
//		
//	}
//
//	private void sendRoutingTable() {
//
//		try {
//			out.writeObject(routingTable);
//			out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//
//	private void updateRoutingTable(HelloPacket node) {
//		
//		boolean isAlreadyThere = false;
//		for(int i = 0;i<routingTable.size();i++){
//			if((routingTable.get(i).IP.equals(node.IP)) && (routingTable.get(i).port == node.port))
//				isAlreadyThere = true;
//			
//		}
//		
//		if (!isAlreadyThere)
//			routingTable.add(new RoutingRecord(node.IP, node.port, IS_NOT_ME, node.ID, nodeSocket));
//		
//	}
//
//}
//

