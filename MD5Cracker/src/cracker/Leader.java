package cracker;

import java.io.*;
import java.util.Arrays;

import node.Node;
import node.RoutingRecord;

public class Leader extends Thread{
	
	private StringBuffer hash;
	
	private int firstChar;
	private int lastChar;
	//have to be equal to the number of '*' + 1
	private int freeCharacters;
	
	
	/**
	 * 
	 * @param hashString
	 */
	public Leader(StringBuffer hashString){
		
		hash = hashString;
		
		firstChar = (int)'A';
		lastChar = (int)'z';
		freeCharacters = 4;
		
		boolean isEmpty = true;
		for(int i = 0; i < Node.getQueue().length; i++){
			if(Node.getQueue()[i]!= null){
				isEmpty = false;
			}
		}
		
		if(isEmpty){
			initQueue();
		}
		
	}
	
	
	/**
	 * 
	 * @param hashString
	 * @param first
	 * @param last
	 * @param freeChars
	 */
	public Leader(StringBuffer hashString, int first, int last, int freeChars){
		
		hash = hashString;
		
		firstChar = first;
		lastChar = last;
		freeCharacters = freeChars;
		
		boolean isEmpty = true;
		for(int i = 0; i < Node.getQueue().length; i++){
			if(Node.getQueue()[i]!= null){
				isEmpty = false;
			}
		}
		
		if(isEmpty){
			initQueue();
		}
		
	}
	
	
	/**
	 * 
	 */
	private void initQueue(){
		
		for(int i=0; i<Node.getQueue().length;i++){
			Node.getQueue()[i] = new QueueRecord();
		}		
		
		Node.getQueue()[0].str = "***";
		Node.getQueue()[1].str = (char)firstChar+Node.getQueue()[0].str;
		
		for (int i = 2; i < Node.getQueue().length; i++){
			Node.getQueue()[i].str = this.createNextString(Node.getQueue()[i-1].str);
		}
	}
	
	
	/**
	 * 
	 */
	public void setStringInArray(String newString, int position){		
		if(position < Node.getQueue().length && position >= 0){			
			Node.getQueue()[position].str = newString;			
		}		
	}
	
	
	/**
	 * 
	 * @param isStarted
	 * @param position
	 */
	public void setStartedInArray(boolean isStarted, int position){		
		if(position < Node.getQueue().length && position >= 0){			
			Node.getQueue()[position].isStarted = isStarted;			
		}		
	}
	
	
	/**
	 * 
	 * @param isFinished
	 * @param position
	 */
	public void setFinishedInArray(boolean isFinished, int position){		
		if(position < Node.getQueue().length && position >= 0){			
			Node.getQueue()[position].isFinished = isFinished;			
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
	 * data structure is rebuilt, checked elements are deleted and the queue is
	 * re-populated started from the last valid element in the list
	 */
	public synchronized void reconstructParsedString(int computedElements){
		
		if(computedElements > 0){
			
			/*
			 * firstly the array is sorted and the already checked elements are put
			 * at the end
			 */
			Arrays.sort(Node.getQueue(), new ParsedStringCoparator());
			
			/*
			 * then starting form the last valid element the queue is repopulated
			 */
			for(int i = Node.getQueue().length - computedElements; i < Node.getQueue().length; i++){
				Node.getQueue()[i].str = "";
				Node.getQueue()[i].checkString = null;
				Node.getQueue()[i].isStarted = false;
				Node.getQueue()[i].isFinished = false;
				Node.getQueue()[i].ipComputing = null;
				Node.getQueue()[i].portComputing = 0;
				Node.getQueue()[i].str=createNextString(Node.getQueue()[i-1].str);
			}
		}
		
	}
	
	/**
	 * send a range of string to a node
	 * 
	 * @param qRecord
	 * @param rr
	 */
	public void sendStringToNode(QueueRecord qRecord, RoutingRecord rr){
		
		try {
			
			//generate a String for the liar detection and save it
			String checkingString = qRecord.str.substring(0,1+qRecord.str.length()-freeCharacters);
			
			for(int i = 0; i < freeCharacters-1; i++){
				String random =String.format("%c", (char)(firstChar + (Math.random()*(1+lastChar-firstChar))));
				checkingString = checkingString.concat(random);
			}
			
			qRecord.checkString = checkingString;
			
			System.out.printf("Sending %s - %s to %d\n", checkingString, qRecord.str, rr.port);
			
			//send the string to the node
			SendingStrings ss = new SendingStrings(hash.toString(), StringChecker.encode(checkingString), qRecord.str, freeCharacters, firstChar, lastChar);
			
			ObjectOutputStream out = new ObjectOutputStream(rr.socket.getOutputStream());
			out.writeObject(ss);
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * check if the method has effectively computed some work
	 * 
	 * @param result
	 * @param solution
	 */
	public static void checkSolution(String[] result, StringBuffer solution){
		
		//check which routing record should be updated
		
		//for each record in the queue check whether its ip is equal to the given ip
		for(QueueRecord qRecord : Node.getQueue()){
			if(qRecord!= null && qRecord.ipComputing != null && qRecord.ipComputing.equals(result[2]) && qRecord.portComputing == Integer.parseInt(result[3])){
				
				
				if(!qRecord.isFinished && result[1].equals(qRecord.checkString)){
					System.out.printf("%s:%s not liar %s == %s!!", result[2], result[3], result[1], qRecord.checkString);
					
					//if there is a solution stop everything
					if(result[0] != null){
						System.out.printf("The key is: %s\n", result[0]);
						if(solution != null)
							solution.replace(0, solution.length(), result[0]);
						else
							System.out.println("ERROR");
						Node.setHasLeader(false);
					}
					
					
					//set the queuerecord as computed and the node as non calculating
					for(RoutingRecord rr : Node.getRoutingTable()){
						if(rr.IP.equals(result[2]) && rr.port == Integer.parseInt(result[3])){
							
							rr.isComputing = false;
							qRecord.isFinished = true;
							
							System.out.printf(" ready for a new job\n");
						}
						
					}
					
				//if the node is a liar it will set as working and no other job 
				//will be assigned to it, and the work it was computing set as not started
				} else if(!qRecord.isFinished && !result[1].equals(qRecord.checkString)){
					System.out.printf("%s:%s liar you gave me %s instead of %s!!\n", result[2], result[3], result[1], qRecord.checkString);
					qRecord.isStarted = false;
				}
				
			}
		}
		
	}
	
	/**
	 * send to every node in the routing table a range of strings to check and wait
	 * for their answer
	 */
	public void run(){
		
		do{
		
			//for each non computing node there will be assigned a work
			for(RoutingRecord rr : Node.getRoutingTable()){
				
				if(!rr.isComputing && !rr.isMe){
					
					synchronized(Node.getQueue()){
						
						for(int j = 0; j < Node.getQueue().length; j++){
							QueueRecord qRecord = Node.getQueue()[j];
							//the last string in the queue should not be passed 
							//because it used as base for the creation of the next strings 
							if(!qRecord.isStarted && j!=(Node.getQueue().length-1)){
								
								sendStringToNode(qRecord, rr);
								qRecord.isStarted = true;
								qRecord.ipComputing = rr.IP;
								qRecord.portComputing = rr.port;

								rr.isComputing = true;
								break;
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
			 * reconstruct the queue if the number of computed elements id > than
			 * the half of the length
			 */
			
			QueueRecord[] queueCopy;
			
			synchronized(Node.getQueue()){
				
				int count = 0;
				
				for(QueueRecord ps : Node.getQueue()){
					if(ps.isFinished){
						count++;
					}
				}
				
				if (count > Node.getQueue().length/2){
					reconstructParsedString(count);
					queueCopy = Node.getQueue().clone();
					
					// clears the queue copy and broadcasts it
					for(int i = 0; i < queueCopy.length; i++){
						queueCopy[i].isFinished = false;
						queueCopy[i].isFinished = false;
						queueCopy[i].ipComputing = null;
						queueCopy[i].portComputing = 0;
					}
			
					System.out.println("Broadcasting the routing table");
					Node.broadcastObject(Node.getRoutingTable(), queueCopy);
				}
				
				
				
			}//synch end
			
			
			
		
		}while(Node.getHasLeader());
		
		System.out.println("key decoded, it was: " + hash.toString());
		
		System.out.println("Work finished\nInsert the hash to decode");
		
		//clear the queue, send it and set it as null
		for(int i = 0; i < Node.getQueue().length; i++){
			Node.getQueue()[i] = null;
		}
		//set every routing record as non Leader
		for(RoutingRecord rr : Node.getRoutingTable()){
			rr.isLeader = false;
			rr.isComputing = false;
		}
		Node.broadcastObject(Node.getRoutingTable(), Node.getQueue());
//		Node.setQueue(null);
	}
	

}
