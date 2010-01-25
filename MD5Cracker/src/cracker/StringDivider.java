package cracker;

import java.util.Arrays;

public class StringDivider {

	public static final int PARSED_STRING_LENGTH = 10;
	public static final int FIRST_CHAR = 65;
	public static final int LAST_CHAR = 66;
	//have to be equal to the number of * + 1
	public static final int FREE_CHARACTERS = 4;
	
	private QueueRecord[] testedStrings = new QueueRecord[PARSED_STRING_LENGTH];
	
	public StringDivider(){
		
		for(int i=0; i<PARSED_STRING_LENGTH;i++){
			testedStrings[i] = new QueueRecord();
		}		
		
		testedStrings[0].str = "***";
		testedStrings[1].str = (char)FIRST_CHAR+testedStrings[0].str;
		
		for (int i = 2; i < PARSED_STRING_LENGTH; i++){
			testedStrings[i].str = this.createNextString(testedStrings[i-1].str);
		}
		
	}
	
	
	public QueueRecord[] getTestedStrings(){		
		return this.testedStrings;		
	}
	
	public void setStringInArray(String newString, int position){		
		if(position < PARSED_STRING_LENGTH && position >= 0){			
			this.testedStrings[position].str = newString;			
		}		
	}
	
	public void setStartedInArray(boolean isStarted, int position){		
		if(position < PARSED_STRING_LENGTH && position >= 0){			
			this.testedStrings[position].isStarted = isStarted;			
		}		
	}
	
	public void setFinishedInArray(boolean isFinished, int position){		
		if(position < PARSED_STRING_LENGTH && position >= 0){			
			this.testedStrings[position].isFinished = isFinished;			
		}		
	}
	
	/**
	 * given a string the method returns the next string according to preset 
	 * parameters
	 * 
	 * @param s
	 * @return
	 */
	public String createNextString(String s){
		
		//it goes to the first char different from * from the right
		int position = s.length()-FREE_CHARACTERS;
		
		char charToCheck = s.charAt(position);
		
		//if charToCheck is the last char of our set of chars
		if( charToCheck == (char) LAST_CHAR){			
			
			s = recurseLastChar(s, position, charToCheck);
			
		} else {
			//substitute the first char from the right that is not '*' with the 
			//following one in the unicode table
			s = s.substring(0, position)+ (char)(charToCheck+1) + s.substring(position+1);
		}
		
		return s;
		
	}
	
	/**
	 * the method changes all the chars which are equal to the final char in the
	 * set of chars to take into account, eventually adding the first char at the
	 * beginning if the string is composed by only "last" chars
	 * 
	 * @param s
	 * @param position
	 * @param charToCheck
	 * @return
	 */
	public String recurseLastChar(String s, int position, char charToCheck){
		
		/*
		 * fist of all substitute the last with the first
		 * e.g. first = 'a' last = 'z'
		 * if the passed string is "bbbbz***" this immediately becomes "bbbba***" 
		 */
		s = s.substring(0, position)+ (char)FIRST_CHAR + s.substring(position+1);
		
		
		/*
		 *  if we are in the first position we add at the beginning the first
		 *  char of our set
		 *  e.g first = 'a' last = 'z'
		 *  firstly the passed string was "z***", it is then immediately transformed
		 *  to "a***" and then since we were parsing the char at position 0 
		 *  (first char of the string) we add in front of it the first char of
		 *  our set, thus the string becomes "aa***" which was what we were
		 *  interested in
		 */
		if(position == 0){
			s = (char)FIRST_CHAR + s;
		
			
		/*
		 * if we are not in the first position of the string we go backwards and 
		 * if the char in that position is the last char we recursively call this
		 * method, otherwise we simply substitute the char with its following one
		 * on the list
		 * e.g. first 'a'   last 'z'
		 * at the beginning the string was "bbbbz***" then it has become "bbbba***"
		 * now we go backwards and we substitute the first 'b' from the right with
		 * its successor and the string becomes "bbbca***"
		 */
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
	
	
	/**
	 * when some of the elements in the data structure are checked the whole
	 * data structure is rebuilt, checked elements are deleted and the queue is
	 * re-populated started from the last valid element in the list
	 */
	public void reconstructParsedString(int computedElements){
		
		if(computedElements > 0){
			
			/*
			 * firstly the array is sorted and the already checked elements are put
			 * at the end
			 */
			Arrays.sort(testedStrings, new ParsedStringCoparator());
			
			/*
			 * then starting form the last valid element the queue is repopulated
			 */
			for(int i = PARSED_STRING_LENGTH - computedElements; i < PARSED_STRING_LENGTH; i++){
				testedStrings[i].str = "";
				testedStrings[i].isStarted = false;
				testedStrings[i].isFinished = false;
				testedStrings[i].str=createNextString(testedStrings[i-1].str);
			}
		}
		
	}
	
}
