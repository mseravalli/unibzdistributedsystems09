package node;


import java.io.Serializable;
import java.net.Socket;

public class RoutingRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -194140748789275619L;
	public static final int NULL_ID = -1;
	public static final boolean IS_ME = true;
	public static final boolean IS_NOT_ME = false;
	
	
	public String IP;
	public int port;
	public boolean isMe;
	public int ID;
	public Socket socket;
	public boolean isLeader;
	public boolean isComputing;
	
	//constructor that sets the IP and sets the ID to null
	public RoutingRecord(String ip, int po,boolean iM){
		IP = ip;
		port = po;
		isMe = iM;
		ID = NULL_ID;
		socket = null;
		isLeader = false;
		isComputing = false;
	}
	
	public RoutingRecord(String ip, int po, boolean iM, int id){
		IP = ip;
		ID = id;
		port = po;
		isMe = iM;
		socket = null;	
		isLeader = false;
		isComputing = false;
	}
	
	public RoutingRecord(String ip, int po, boolean iM, int id, Socket aSocket){
		IP = ip;
		ID = id;
		port = po;
		isMe = iM;
		if(!iM){
			socket = aSocket;
		}
		isLeader = false;
		isComputing = false;		
	}
	
	public RoutingRecord(String ip, int po, boolean iM, int id, Socket aSocket, boolean leader){
		IP = ip;
		ID = id;
		port = po;
		isMe = iM;
		if(!iM){
			socket = aSocket;
		}
		isLeader = leader;
		isComputing = false;		
	}
	
	
}