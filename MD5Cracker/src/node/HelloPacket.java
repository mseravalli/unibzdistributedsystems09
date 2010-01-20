package node;

import java.io.Serializable;

public class HelloPacket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3132136853664077791L;
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
