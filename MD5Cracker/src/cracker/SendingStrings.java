package cracker;

public class SendingStrings {

	public String hash;
	public String checkingHash;
	public int freeChars;
	public int firstChar;
	public int lastChar;
	
	
	public SendingStrings(String h, String ch, int freeCharachters, int fc, int lc){
		hash = h;
		checkingHash = ch;
		freeChars = freeCharachters;
		firstChar = fc;
		lastChar = lc;
	}
	
}
