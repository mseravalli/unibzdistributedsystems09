package cracker;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import node.Node;
import node.RoutingRecord;

public class Leader extends Thread{
	
	private StringBuffer hash;
	
	private int firstChar;
	private int lastChar;
	//have to be equal to the number of * + 1
	private int freeCharacters;
	
	private QueueRecord[] queue;
	
	private ArrayList <RoutingRecord> routingTable;
	private boolean[] hasLeader;
	
//	public Leader(ArrayList <RoutingRecord> rTable, String hashString, int first, int last, int freeChars, boolean[] leader){
//		
//		routingTable = rTable;
//		
//		hash = hashString;
//		
//		firstChar = first;
//		lastChar = last;
//		freeCharacters = freeChars;
//		
//		hasLeader = leader;
//		
//		initStack(20);
//		
//	}
	
	public Leader(ArrayList <RoutingRecord> rTable, StringBuffer hashString, int first, int last, int freeChars, boolean[] leader, QueueRecord[] theQueue){
		
		
		routingTable = rTable;
		
		hash = hashString;
		
		firstChar = first;
		lastChar = last;
		freeCharacters = freeChars;
		
		hasLeader = leader;
		
		queue = theQueue;
		
		boolean isEmpty = true;
		for(int i = 0; i < queue.length; i++){
			if(queue[i]!= null){
				isEmpty = false;
			}
		}
		
		if(isEmpty){
			initQueue();
		}
		
	}
	
	private void initQueue(){
		
		for(int i=0; i<queue.length;i++){
			queue[i] = new QueueRecord();
		}		
		
		queue[0].str = "***";
		queue[1].str = (char)firstChar+queue[0].str;
		
		for (int i = 2; i < queue.length; i++){
			queue[i].str = this.createNextString(queue[i-1].str);
		}
	}
	
	
	public QueueRecord[] getTestedStrings(){		
		return this.queue;		
	}
	
	public void setStringInArray(String newString, int position){		
		if(position < queue.length && position >= 0){			
			this.queue[position].str = newString;			
		}		
	}
	
	public void setStartedInArray(boolean isStarted, int position){		
		if(position < queue.length && position >= 0){			
			this.queue[position].isStarted = isStarted;			
		}		
	}
	
	public void setFinishedInArray(boolean isFinished, int position){		
		if(position < queue.length && position >= 0){			
			this.queue[position].isFinished = isFinished;			
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
			Arrays.sort(queue, new ParsedStringCoparator());
			
			/*
			 * then starting form the last valid element the queue is repopulated
			 */
			for(int i = queue.length - computedElements; i < queue.length; i++){
				queue[i].str = "";
				queue[i].checkString = null;
				queue[i].isStarted = false;
				queue[i].isFinished = false;
				queue[i].ipComputing = null;
				queue[i].portComputing = 0;
				queue[i].str=createNextString(queue[i-1].str);
			}
		}
		
	}
	
	
	public void sendStringToNode(QueueRecord qRecord, RoutingRecord rr){
		
		try {
			
			//generate a String for the liar detection and save it
			String checkingString = qRecord.str.substring(0,1+qRecord.str.length()-freeCharacters);
			
			for(int i = 0; i < freeCharacters-1; i++){
				String random =String.format("%c", (char)(firstChar + (Math.random()*(1+lastChar-firstChar))));
				checkingString = checkingString.concat(random);
			}
			
			qRecord.checkString = checkingString;
			
			System.out.printf("Leader: sending %s - %s to %d\n", checkingString, qRecord.str, rr.port);
			
			//send the string to the node
			SendingStrings ss = new SendingStrings(hash.toString(), StringChecker.encode(checkingString), qRecord.str, freeCharacters, firstChar, lastChar);
			
			ObjectOutputStream out = new ObjectOutputStream(rr.socket.getOutputStream());
			out.writeObject(ss);
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void checkSolution(String[] result, ArrayList <RoutingRecord> rTable, QueueRecord[] aQueue , boolean leader[], StringBuffer solution){
		
		//check which routing record should be updated
		
		//for each record in the queue check whether its ip is equal to the given ip 
		for(QueueRecord qRecord : aQueue){
			if(qRecord.ipComputing != null && qRecord.ipComputing.equals(result[2]) && qRecord.portComputing == Integer.parseInt(result[3])){
				
				
				if(!qRecord.isFinished && result[1].equals(qRecord.checkString)){
					System.out.printf("%s:%s good boy!! %s == %s!!", result[2], result[3], result[1], qRecord.checkString);
					
					//if there is a solution stop everything
					if(result[0] != null){
						System.out.printf("The key is: %s\n", result[0]);
						if(solution != null)
							solution.replace(0, solution.length(), result[0]);
						else
							System.out.println("dio poi!!");
						leader[0]= false;
					}
					
					
					//set the queuerecord as computed and the node as non calculating
					for(RoutingRecord rr : rTable){
						if(rr.IP.equals(result[2]) && rr.port == Integer.parseInt(result[3])){
							
							rr.isComputing = false;
							qRecord.isFinished = true;
							
							System.out.printf("%s:%d ready for a new job\n", rr.IP, rr.port);
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
	
	
	public void run(){
		
		do{
		
			//for each non computing node there will be assigned a work
			for(RoutingRecord rr : routingTable){
				
				if(!rr.isComputing && !rr.isMe){
					
					synchronized(queue){
						
						for(int j = 0; j < queue.length; j++){
							QueueRecord qRecord = queue[j];
							if(!qRecord.isStarted && j!=(queue.length-1)){
								
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
			
			synchronized(queue){
				
				int count = 0;
				
				for(QueueRecord ps : queue){
					if(ps.isFinished){
						count++;
					}
				}
				
				if (count > queue.length/2){
					reconstructParsedString(count);
					queueCopy = queue.clone();
					
					// clears the queue copy and broadcasts it
					for(int i = 0; i < queueCopy.length; i++){
						queueCopy[i].isFinished = false;
						queueCopy[i].isFinished = false;
						queueCopy[i].ipComputing = null;
						queueCopy[i].portComputing = 0;
					}
			
					Node.broadcastObject(routingTable, queueCopy);
				}
				
				
				
			}//synch end
			
			
			
		
		}while(hasLeader[0]);
		
		System.out.println("key decoded, it was: " + hash.toString());
		
		//clear the queue, send it and set it as null
		for(int i = 0; i < queue.length; i++){
			queue[i] = null;
		}
		Node.broadcastObject(routingTable, queue);
		queue = null;
	}
	

}
