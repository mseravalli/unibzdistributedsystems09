package Node;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.util.Enumeration;


public class Node extends Thread{
	
	
	
	
	public Node(){
		
	}
	
	public void run(){
		
	}
	
	//Recursively checks all Strings in a certain range
	public void checkStrings(String toFind, String rng, int noOfUnknown){
		
		//base case - we've only one variable char left
		if (noOfUnknown == 0){
			System.out.println(rng);
			encode(rng,toFind);
		}
		
		//recursive case - we don't know how many variable chars left
		else if(noOfUnknown > 0){
			for(int i = 32; i< 127;i++){
				String currentChar = new Character((char)i).toString();
				String testString = rng.concat(currentChar);
				checkStrings(toFind, testString,noOfUnknown -1);
			}
		}
	}
	
	
	
	//Encodes a certain String and compares it to the searched String, 
	//if they are the same (key was cracked), then it returns the original msg
	//else it returns null
	public String encode(String toTest, String toFind){
		
		String encoded = "";
        byte a[] = toTest.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte hash[] = md.digest(a);
            for (byte b: hash) {
                encoded = encoded + String.format("%02x",b);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            System.exit(1);
        };
        if (encoded == toFind)
        	return toTest;
        else 
        	return null;
        
	}
	
	

	
	
}
