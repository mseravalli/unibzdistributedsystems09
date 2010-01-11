package cracker;

import java.util.Comparator;

public class RoutingRecord {
	public String IP;
	public int ID;
	
	
	//constructor that sets the IP and sets the ID to null
	public RoutingRecord(String p){
		IP = p;
		ID = -1;
	}

}

//class IntRRCoparator implements Comparator<Object>{
//
//	@Override
//	public int compare(Object rr1, Object rr2){
//
//		//parameter are of type Object, so we have to downcast it to Employee objects
//
//		int rr1ID = ( (RoutingRecord) rr1).ID;
//		
//		int rr2ID = ( (RoutingRecord) rr2).ID;
//
//		if( rr1ID > rr2ID )
//			return 1;
//
//		else if( rr1ID < rr2ID )
//			return -1;
//
//		else
//			return 0;
//
//	}
//	
//	
//	
//}