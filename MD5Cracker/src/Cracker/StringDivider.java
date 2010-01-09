package Cracker;

import java.util.Arrays;

public class StringDivider {

	public static final int PARSED_STRING_LENGTH = 10;
	public static final int FIRST_CHAR = 65;
	public static final int LAST_CHAR = 66;
	//have to be equal to the number of * + 1
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
	
	public void setStringInArray(String newString, int position){
		
		if(position < PARSED_STRING_LENGTH && position >= 0){
			
			this.testedStrings[position].str = newString;
			
		}
		
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
		
		//count number of free slots
		int freeSlots = 0;
		for (int i = 0; i < PARSED_STRING_LENGTH; i++){
			if(testedStrings[i].str.equals(""))
				freeSlots++;
		}
		
		for(int i = 0; i < PARSED_STRING_LENGTH-freeSlots; i++){				
			testedStrings[i]=testedStrings[i+freeSlots].clone();
		}
		
		for(int i = freeSlots; i < PARSED_STRING_LENGTH; i++){				
			testedStrings[i].str=createNextString(testedStrings[i-1].str);
		}
		
	}
	
	
	public static void main(String[] args){
		
		StringDivider sd = new StringDivider();
		
		
		ParsedString[] testedStrings = sd.getTestedStrings();
		/*
		testedStrings[2].str = "";		
		sd.reconstructParsedString();			
		
		testedStrings[2].str = "";			
		sd.reconstructParsedString();
		
		testedStrings[2].str = "";		
		sd.reconstructParsedString();	
		
		testedStrings[2].str = "";		
		sd.reconstructParsedString();*/
		
		
		for(int i = 0; i < PARSED_STRING_LENGTH; i++){
			System.out.println(testedStrings[i].str);
		}		
		
		
	}
	
}
