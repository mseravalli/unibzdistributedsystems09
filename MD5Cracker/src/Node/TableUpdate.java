//package Node;
//
//import java.util.ArrayList;
//
//import node.RoutingRecord;
//
//
//public class TableUpdate extends Thread{
//	
//	private ArrayList <RoutingRecord> routingTable;
//	
//	public TableUpdate(ArrayList <RoutingRecord> rT){
//		routingTable = rT;
//	}
//	
//	public void run(){
//		
//		while(true){
//			for(int i = 0;i<routingTable.size();i++){
//				if(routingTable.get(i).age <= 4){
//					routingTable.get(i).age++;
//				}
//				else{
//					routingTable.remove(i);
//				}
//			}
//			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
//
//}