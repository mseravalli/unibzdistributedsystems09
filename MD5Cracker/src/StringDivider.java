
import java.util.Arrays;

public class StringDivider {

	/* this is set to 96 because there are 95 characters + the special case for 
	 * the first string and the strings to remember*/
	public static final int PARSED_STRING_LENGTH = 5;
	public static final int FIRST_CHAR = 65;
	public static final int LAST_CHAR = 68;
	
	private ParsedString[] testedStrings = new ParsedString[PARSED_STRING_LENGTH];
	
	public StringDivider(){
		for(int i=0; i<PARSED_STRING_LENGTH;i++){
			testedStrings[i] = new ParsedString();
		}
		
		
		testedStrings[0].str = "***";
		
		for(int i=1; i<PARSED_STRING_LENGTH;i++){
			
			char c = (char) (i+64);
			
			/* '\' is used as escape sequence in front of '*' and '\' itself */
			if (c == '*' || c =='\\')
				testedStrings[i].str="\\"+c+testedStrings[0].str;
			else
				testedStrings[i].str=c+testedStrings[0].str;
			
		}
		
		
	}
	
	public ParsedString[] getTestedStrings(){		
		return this.testedStrings;		
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
			//s.charAt(s.length()-3);
			
			int position = s.length()-4;
			
			char charToCheck = s.charAt(s.length()-4);
						
			if( charToCheck == (char) LAST_CHAR){
				
				s = (char)FIRST_CHAR + s.substring(1);
				
				if(position == 0){
					s = (char)FIRST_CHAR + s;
				}
			} else {
				//System.out.println(charToCheck);(char)(charToCheck+1)
				s = s.substring(0, position)+ (char)(charToCheck+1) + s.substring(position+1);
			}
			
			
			
			testedStrings[PARSED_STRING_LENGTH-1].str = s;
			
			System.out.printf("resulting string: %s\n",s);
			
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
