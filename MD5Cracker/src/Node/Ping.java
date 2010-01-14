package Node;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cracker.RoutingRecord;

public class Ping extends Thread {

	private static final boolean IS_HELLO = false;
	private Socket asocket;
	private ObjectOutputStream out;
	private ArrayList<RoutingRecord> routingTable;
	private RoutingRecord actualRecord;
	private HelloPacket actualHello;
	
	public Ping(ArrayList<RoutingRecord> rT) {
		routingTable = rT;
	}
	
	// da quello che ho capito ping si collega ogni volta con un nodo e manda
	// un pacchetto, non è meglio tenere le connessioni vecchie?
	
	//si hai ragione, in effetti non é un problema, visto che abbiamo tutti 
	//socket nella routing table
	public void run(){
		
		while(true){
			
			for(int i = 0; i<routingTable.size();i++){
				
				actualRecord = routingTable.get(i);
				
				if(!actualRecord.isMe){
					actualHello = new HelloPacket(actualRecord.IP,actualRecord.port,IS_HELLO,actualRecord.ID);
					try {
						asocket = actualRecord.socket;
						out = new ObjectOutputStream(asocket.getOutputStream());
						out.writeObject(actualHello);
						out.flush();
						out.close();
						out = null;
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
