package cracker;
import java.io.Serializable;
import java.util.Comparator;


public class QueueRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3802535699132233481L;
	public String str;
	public boolean isStarted;
	public boolean isFinished;
	public String checkString;
	
	public String ipComputing;
	public int portComputing;
	
	public QueueRecord(){
		str = null;
		isStarted = false;
		isFinished = false;
		checkString = null;
		
		ipComputing = null;
		portComputing = 0;
	}
	
}

class ParsedStringCoparator implements Comparator<Object>{

	public int compare(Object str1, Object str2){

		//parameter are of type Object, so we have to downcast it to StringCoparator objects
	
		String firstStr = ( (QueueRecord) str1 ).str;
	
		String secondStr = ( (QueueRecord) str2 ).str;
		
		boolean isFisrtStarted = ( (QueueRecord) str1 ).isStarted;
		boolean isFisrtFinished = ( (QueueRecord) str1 ).isFinished;
		boolean isFirstComputed = isFisrtStarted && isFisrtFinished;
		
		boolean isSecondStarted = ( (QueueRecord) str2 ).isStarted;		
		boolean isSecondFinished = ( (QueueRecord) str2 ).isFinished;
		boolean isSecondComputed = isSecondStarted && isSecondFinished;
		
	
		//uses compareTo method of String class to compare names of the StringCoparator
		
		/*
		 * if both strings are computed or both are not, they are comparated
		 * as two strings
		 */
		if((!isFirstComputed && !isSecondComputed) || (isFirstComputed && isSecondComputed)){
			
			if(firstStr.length() == secondStr.length()){
				return firstStr.compareTo(secondStr);
			} else if(firstStr.length() < secondStr.length()){
				return -1;
			} else {
				return 1;
			}
		
			
		/*
		 * if one of the two strings is already computed that one will be put
		 * at the end of the array
		 */
		} else {
			if (isFirstComputed && !isSecondComputed){
				return 1;
			} else {
				return -1;
			}
		}

	}

}