
import java.util.Arrays;

public class StringDivider {

	/* this is set to 96 because there are 95 characters + the special case for 
	 * the first string and the strings to remember*/
	public static final int PARSED_STRING_LENGTH = 7;
	public static final int FIRST_CHAR = 65;
	public static final int LAST_CHAR = 66;
	public static final int FREE_CHARACTERS = 4;
	
	private ParsedString[] testedStrings = new ParsedString[PARSED_STRING_LENGTH];
	
	public StringDivider(){
		for(int i=0; i<PARSED_STRING_LENGTH;i++){
			testedStrings[i] = new ParsedString();
		}
		
		
		testedStrings[0].str = "***";
		testedStrings[1].str = (char)FIRST_CHAR+testedStrings[0].str;
		
		for (int i = 2; i < PARSED_STRING_LENGTH; i++){
			testedStrings[i].str = this.createNextString(testedStrings[i-1].str);
		}
		
	}
	
	
	public ParsedString[] getTestedStrings(){		
		return this.testedStrings;		
	}
	
	
	public String createNextString(String s){
		
		int position = s.length()-FREE_CHARACTERS;
		
		char charToCheck = s.charAt(position);
		
		
		if( charToCheck == (char) LAST_CHAR){			
			
			s = recurseLastChar(s, position, charToCheck);
			
		} else {
			//substitute the first char from the right that is not '*'
			s = s.substring(0, position)+ (char)(charToCheck+1) + s.substring(position+1);
		}
		
		return s;
		
	}
	
	
	public String recurseLastChar(String s, int position, char charToCheck){
		
		s = s.substring(0, position)+ (char)FIRST_CHAR + s.substring(position+1);
		
		if(position == 0){
			s = (char)FIRST_CHAR + s;
		} else {
			position--;
			charToCheck = s.charAt(position);
			if( charToCheck == (char) LAST_CHAR)
				s = recurseLastChar(s, position, charToCheck);
			else
				s = s.substring(0, position)+ (char)(charToCheck+1) + s.substring(position+1);		
		}
		
		return s;
		
	}
	
	
	/* when all the elements in the data structure are checked a new datastructure
	 * will be created*/
	public void reconstructParsedString(){
		
		Arrays.sort(testedStrings, new StringCoparator());
		
		if(testedStrings[0].str.equals("")){
			//the empty string is put at the end
			for(int i = 0; i < PARSED_STRING_LENGTH-1; i++){				
				testedStrings[i]=testedStrings[i+1].clone();
				//System.out.println(testedStrings[i].str);
			}
			
			testedStrings[PARSED_STRING_LENGTH-1] = new ParsedString();
			
			
			
			
			
			//create the last string
			
			String s = testedStrings[PARSED_STRING_LENGTH-2].str;
			
			testedStrings[PARSED_STRING_LENGTH-1].str = this.createNextString(s);
			
			
		}
		
		
		
	}
	
	
	public static void main(String[] args){
		
		StringDivider sd = new StringDivider();
		
		
		ParsedString[] testedStrings = sd.getTestedStrings();
		
		testedStrings[2].str = "";		
		sd.reconstructParsedString();			
		
		testedStrings[2].str = "";			
		sd.reconstructParsedString();
		
		testedStrings[2].str = "";		
		sd.reconstructParsedString();	
		
		testedStrings[2].str = "";		
		sd.reconstructParsedString();
		
		
		for(int i = 0; i < PARSED_STRING_LENGTH; i++){
			System.out.println(testedStrings[i].str);
		}
			
		
		
		
	}
	
}
