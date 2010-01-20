package cracker;

import java.io.Serializable;

public class SendingStrings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8342462638967937823L;
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
