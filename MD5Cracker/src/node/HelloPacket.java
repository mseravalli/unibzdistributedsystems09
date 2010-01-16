package node;

import java.io.Serializable;

public class HelloPacket implements Serializable{
	
	public boolean isHello;
	public String IP;
	public int ID;
	public int port;
	
	public HelloPacket(String ip, int po, boolean hi, int id){
		IP = ip;
		port = po;
		ID = id;
		isHello = hi;
		
	}
}
