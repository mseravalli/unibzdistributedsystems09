package node;

import java.security.MessageDigest;

public class StringChecker {
	
	private static String[] results;
	
	public static String[] compute(String toFind, String check, String prefix, int first, int last, int noOfUnknown){
		
		results = new String[2];
		results[0] = null;
		results[1] = null;
		String rng = prefix;
		
		if(prefix.equals("")){
			for(int i = noOfUnknown; i>0; i--){
				checkStrings(toFind,check,"",first,last,i);
			}
		}
		else
			checkStrings(toFind, check, rng, first, last, noOfUnknown);
		
		return results;
		
	}
	
	
	//Recursively checks all Strings in a certain range
	private static void checkStrings(String toFind, String check, String rng, int first, int last, int noOfUnknown){
		
		
		
		//base case - we've only one variable char left
		if (noOfUnknown == 0){
			System.out.println(rng);
			if(encode(rng,toFind) != null)
				results[0] = encode(rng,toFind);
			if(encode(rng,check) != null)
				results[1] = encode(rng,check);
		}
		
		//recursive case - we don't know how many variable chars left
		else if(noOfUnknown > 0){
			for(int i = first; i< last;i++){
				String currentChar = new Character((char)i).toString();
				String testString = rng.concat(currentChar);
				checkStrings(toFind, check, testString, first, last, noOfUnknown -1);
			}
		}
	}
	
	
	
	//Encodes a certain String and compares it to the searched String, 
	//if they are the same (key was cracked), then it returns the original message
	//else it returns null
	private static String encode(String toTest, String toFind){
		
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
