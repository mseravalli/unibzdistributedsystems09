package cracker;
import java.io.Serializable;
import java.util.Comparator;


public class StackRecord{

	public String str;
	public boolean isStarted;
	public boolean isFinished;
	public String checkString;
	
	public String ipComputing;
	public int portComputing;
	
	public StackRecord(){
		str = null;
		isStarted = false;
		isFinished = false;
		checkString = null;
	}
	
//	public StackRecord clone(){
//		
//		StackRecord ps = new StackRecord();
//		
//		ps.str = this.str;
//		ps.isStarted = this.isStarted;
//		ps.isFinished = this.isFinished;
//		
//		return ps;
//	}
	
}

class ParsedStringCoparator implements Comparator<Object>{

	public int compare(Object str1, Object str2){

		//parameter are of type Object, so we have to downcast it to StringCoparator objects
	
		String firstStr = ( (StackRecord) str1 ).str;
	
		String secondStr = ( (StackRecord) str2 ).str;
		
		boolean isFisrtStarted = ( (StackRecord) str1 ).isStarted;
		boolean isFisrtFinished = ( (StackRecord) str1 ).isFinished;
		boolean isFirstComputed = isFisrtStarted && isFisrtFinished;
		
		boolean isSecondStarted = ( (StackRecord) str2 ).isStarted;		
		boolean isSecondFinished = ( (StackRecord) str2 ).isFinished;
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