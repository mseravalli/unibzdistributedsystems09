
public class StringDivider {

	/* this is set to 96 because there are 95 characters + the special case for 
	 * the first string and the strings to remember*/
	private final int parsedStringLength = 96;
	
	private ParsedString[] testedStrings = new ParsedString[parsedStringLength];
	
	public StringDivider(){
		for(int i=0; i<parsedStringLength;i++){
			testedStrings[i] = new ParsedString();
		}
	}
	
	public void divideString(){		
		
		testedStrings[0].str = "***";
		
		for(int i=1; i<parsedStringLength;i++){
			
			char c = (char) (i+31);
			
			/* '\' is used as escape sequence in front of '*' and '\' itself */
			if (c == '*' || c =='\\')
				testedStrings[i].str="\\"+c+testedStrings[0].str;
			else
				testedStrings[i].str=c+testedStrings[0].str;
			
			System.out.println(testedStrings[i].str);
		}
		
	}
	
	
	/* when all the elements in the data structure are checked a new datastructure
	 * will be created*/
	public void reconstructParsedString(){
		
		boolean areComputed = true;
		
		/* it checks whether all string in the data structure have been computed*/
		for(int i=0; i<parsedStringLength; i++){
			areComputed = testedStrings[i].isComputed && areComputed;
		}
		
		
		if (areComputed){
			//TODO
		}
		
	}
	
	
	public static void main(String[] args){
		
		StringDivider sd = new StringDivider();
		
		sd.divideString();
		
		
	}
	
}
