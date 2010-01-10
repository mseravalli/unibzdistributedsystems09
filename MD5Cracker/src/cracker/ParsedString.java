package cracker;
import java.util.Comparator;


public class ParsedString {

	public String str;
	public boolean isStarted;
	public boolean isFinished;
	
	public ParsedString(){
		str = "";
		isStarted = false;
		isFinished = false;
	}
	
	public ParsedString clone(){
		
		ParsedString ps = new ParsedString();
		
		ps.str = this.str;
		ps.isStarted = this.isStarted;
		ps.isFinished = this.isFinished;
		
		return ps;
	}
	
}

class ParsedStringCoparator implements Comparator<Object>{

	public int compare(Object str1, Object str2){

		//parameter are of type Object, so we have to downcast it to StringCoparator objects
	
		String firstStr = ( (ParsedString) str1 ).str;
	
		String secondStr = ( (ParsedString) str2 ).str;
		
		boolean isFisrtFinished = ( (ParsedString) str1 ).isFinished;
		boolean isSecondFinished = ( (ParsedString) str2 ).isFinished;
	
		//uses compareTo method of String class to compare names of the StringCoparator
		
		/*
		 * if both strings are not finished or both are finished they are comparated
		 * as two strings
		 */
		if((!isFisrtFinished && !isSecondFinished) || (isFisrtFinished && isSecondFinished)){
			
			if(firstStr.length() == secondStr.length()){
				return firstStr.compareTo(secondStr);
			} else if(firstStr.length() < secondStr.length()){
				return -1;
			} else {
				return 1;
			}
		
			
		/*
		 * if one of the two strings is already finished that one will be put
		 * at the end of the array
		 */
		} else {
			if (isFisrtFinished && !isSecondFinished){
				return 1;
			} else {
				return -1;
			}
		}

	}

}