
public class Node {
	
	private String result;
	private String range;
	private int noOfUnknows;
	
	public Node(){
		checkStrings("AA***");
	}
	
	public static void main(String[] args){
    	Node n = new Node();
    }
	
	
	//Checks all Strings in a certain range
	public void checkStrings(String rng){
		
		//base case - we've only one variable char left
		if (!(rng.endsWith("*"))){
			System.out.println(rng);
			//encode(rng);
		}
		
		//recursive case - we don't know how many variable chars left
		else if(rng.endsWith("*")){
			for(int i = 32; i< 128;i++){
				String currentChar = new Character((char)i).toString();
				String testString = rng.replaceFirst("*", currentChar);
				System.out.println(testString);
				checkStrings(testString);
			}
		}
	}
	
	
}
