package node;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class InputReceiver extends Thread {
	
	private ObjectInputStream in;
	
	public InputReceiver(ObjectInputStream inStr){
		in = inStr;
	}
	
	public void run(){
		
		System.out.println("InputReceiver: ready to receive data");
		
		try { 
			
			while(true){
		    
				Object o = in.readObject();
		    	
			}
		    
		} catch(EOFException e) {
                    
			
//			int i = this.clients.indexOf(this.clientSocket);
//			System.out.println("Client "+this.nicknames.get(i)+" disconnected");
//			this.nicknames.remove(i);
//			this.clients.remove(this.clientSocket);
			

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
