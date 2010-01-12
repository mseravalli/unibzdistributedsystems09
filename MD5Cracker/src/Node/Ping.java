package Node;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cracker.RoutingRecord;

public class Ping extends Thread{

	private static final boolean IS_HELLO = false;
	private Socket asocket;
	private ObjectOutputStream out;
	private ArrayList<RoutingRecord> routingTable;
	private RoutingRecord actualRecord;
	private HelloPacket actualHello;
	
	public Ping(ArrayList<RoutingRecord> rT) {
		routingTable = rT;
	}
	
	public void run(){
		
		while(true){
			
			for(int i = 0; i<routingTable.size();i++){
				
				actualRecord = routingTable.get(i);
				actualHello = new HelloPacket(actualRecord.IP,actualRecord.port,IS_HELLO,actualRecord.ID);
				try {
					asocket = new Socket(actualRecord.IP,actualRecord.port);
					out = new ObjectOutputStream(asocket.getOutputStream());
					out.writeObject(actualHello);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
	}
}
