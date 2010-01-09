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

class StringCoparator implements Comparator<Object>{

	public int compare(Object str1, Object str2){

		//parameter are of type Object, so we have to downcast it to StringCoparator objects
	
		String firstStr = ( (ParsedString) str1 ).str;
	
		String secondStr = ( (ParsedString) str2 ).str;
	
		//uses compareTo method of String class to compare names of the StringCoparator
		
		if(firstStr.length() == secondStr.length()){
			return firstStr.compareTo(secondStr);
		} else if(firstStr.length() < secondStr.length()){
			return -1;
		} else {
			return 1;
		}

	}

}