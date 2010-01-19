package cracker;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import node.RoutingRecord;

public class Leader extends Thread{
	
	private int firstChar;
	private int lastChar;
	//have to be equal to the number of * + 1
	private int freeCharacters;
	
	private StackRecord[] stack;
	
	private ArrayList <RoutingRecord> routingTable;
	private boolean[] isComputing;
	
	public Leader(int first, int last, int freeChars, boolean[] computing){
		
		firstChar = first;
		lastChar = last;
		freeCharacters = freeChars;
		
		isComputing = computing;
		
		initStack(20);
		
	}
	
	public Leader(int first, int last, int freeChars, boolean[] computing, StackRecord[] theStack){
		
		firstChar = first;
		lastChar = last;
		freeCharacters = freeChars;
		
		isComputing = computing;
		
		stack = theStack;
		
	}
	
	private void initStack(int dim){
		stack = new StackRecord[dim];
		
		for(int i=0; i<stack.length;i++){
			stack[i] = new StackRecord();
		}		
		
		stack[0].str = "***";
		stack[1].str = (char)firstChar+stack[0].str;
		
		for (int i = 2; i < stack.length; i++){
			stack[i].str = this.createNextString(stack[i-1].str);
		}
	}
	
	
	public StackRecord[] getTestedStrings(){		
		return this.stack;		
	}
	
	public void setStringInArray(String newString, int position){		
		if(position < stack.length && position >= 0){			
			this.stack[position].str = newString;			
		}		
	}
	
	public void setStartedInArray(boolean isStarted, int position){		
		if(position < stack.length && position >= 0){			
			this.stack[position].isStarted = isStarted;			
		}		
	}
	
	public void setFinishedInArray(boolean isFinished, int position){		
		if(position < stack.length && position >= 0){			
			this.stack[position].isFinished = isFinished;			
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
		
		//it goes to the first char different from *
		int position = s.length()-freeCharacters;
		
		char charToCheck = s.charAt(position);
		
		//if charToCheck is the last char of our set of chars
		if( charToCheck == (char) lastChar){			
			
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
		s = s.substring(0, position)+ (char)firstChar + s.substring(position+1);
		
		
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
			s = (char)firstChar + s;
		
			
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
			if( charToCheck == (char) lastChar)
				s = recurseLastChar(s, position, charToCheck);
			else
				s = s.substring(0, position)+ (char)(charToCheck+1) + s.substring(position+1);		
		}
		
		return s;
		
	}
	
	
	/**
	 * when some of the elements in the data structure are checked the whole
	 * data structure is rebuilt, checked elements are deleted and the stack is
	 * re-populated started from the last valid element in the list
	 */
	public synchronized void reconstructParsedString(int computedElements){
		
		if(computedElements > 0){
			
			/*
			 * firstly the array is sorted and the already checked elements are put
			 * at the end
			 */
			Arrays.sort(stack, new ParsedStringCoparator());
			
			/*
			 * then starting form the last valid element the stack is repopulated
			 */
			for(int i = stack.length - computedElements; i < stack.length; i++){
				stack[i].str = "";
				stack[i].isStarted = false;
				stack[i].isFinished = false;
				stack[i].str=createNextString(stack[i-1].str);
			}
		}
		
	}
	
	
	public void sendStringToNode(StackRecord ps, RoutingRecord rr){
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(rr.socket.getOutputStream());
			
			out.writeObject(ps.str);
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void run(){
		
		do{
		
			//for each non computing node there will be assigned a work
			for(RoutingRecord rr : routingTable){
				
				if(!rr.isComputing){
					
					synchronized(stack){
						
						for(StackRecord ps : stack){
							if(!ps.isStarted){
								
								sendStringToNode(ps, rr);
								ps.isStarted = true;
								
							}
						}
						
					}//synch end			
					
				}
			}//for rr end
			

			//waits for work being computed
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			/*
			 * reconstruct the stack if the number of computed elements id > than
			 * the half of the length
			 */
			synchronized(stack){
				
				int count = 0;
				
				for(StackRecord ps : stack){
					if(ps.isFinished){
						count++;
					}
				}
				
				if (count > stack.length){
					reconstructParsedString(count);
				}
				
			}//synch end	
		
		}while(isComputing[0]);
		
		
	}
	

}
