package cracker;

public class SendingStrings {

	public String hash;
	public String checkingHash;
	public String prefix;
	public int freeChars;
	public int firstChar;
	public int lastChar;
	
	
	public SendingStrings(String h, String ch, String pref, int freeCharachters, int fc, int lc){
		hash = h;
		checkingHash = ch;
		prefix = pref;
		freeChars = freeCharachters;
		firstChar = fc;
		lastChar = lc;
	}
	
}
