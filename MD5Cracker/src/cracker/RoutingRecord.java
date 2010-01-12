package cracker;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Comparator;

public class RoutingRecord {
	
	public static final int NULL_ID = -1;
	
	public int ID;
	public String IP;
	public int port;
	public Socket socket;
	
	
	//constructor that sets the IP and sets the ID to null
	public RoutingRecord(String ip, int po){
		IP = ip;
		ID = NULL_ID;
		port = po;
		try {
			socket = new Socket(IP,port);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

//class IntRRCoparator implements Comparator<Object>{
//
//	@Override
//	public int compare(Object rr1, Object rr2){
//
//		//parameter are of type Object, so we have to downcast it to Employee objects
//
//		int rr1ID = ( (RoutingRecord) rr1).ID;
//		
//		int rr2ID = ( (RoutingRecord) rr2).ID;
//
//		if( rr1ID > rr2ID )
//			return 1;
//
//		else if( rr1ID < rr2ID )
//			return -1;
//
//		else
//			return 0;
//
//	}
//	
//	
//	
//}