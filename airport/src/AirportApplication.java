import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class AirportApplication extends PostgreSqlAccess{
	
	
	public void updateFlyingTime(){
		try {
			Class.forName(driverName);
		
		
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			System.out.println("I am connected to the database " + dburl + ".");
		
			
			
			String updatePassengerPattern = "BEGIN TRANSACTION;" +
					"UPDATE daily_flight SET departure_date = ? " +
					"WHERE flight_id = ? AND departure_date = ?;" +
					"COMMIT WORK;";
	
			PreparedStatement updatePassengerState = con.prepareStatement(updatePassengerPattern);
			
			String oldDate = "";
			String flightID = "";
			String newDate = "";
			
			updatePassengerState.setString(1, oldDate);
			updatePassengerState.setString(2, flightID);
			updatePassengerState.setString(3, newDate);
			
			
			con.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

    public static void main(String args[]) throws Exception {

		//load the driver 
		Class.forName(driverName);
	
		//establish a connection to the database via the driver
		Connection con = DriverManager.getConnection(dburl, user, passwd);
		
		System.out.println("I am connected to the database " + dburl + ".");
	
		String updatePassengerPattern = "BEGIN TRANSACTION;" +
				"UPDATE trip SET embarked = ? WHERE passport_id = ? AND flight_id = ?;" +
				"COMMIT WORK;";
		
		PreparedStatement updatePassengerState = con.prepareStatement(updatePassengerPattern);
		
		String passportID = "";
		String flightID = "";
		boolean isEmbarked = false;
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("insert passport, flight and if that passenger is" +
				"actually embarked");
		
		try{
			
			System.out.print("passport id: ");
			passportID = "9873648";
			
			System.out.print("flight id: ");
			flightID = "AF6132";
			
			System.out.print("is embarked: ");
			isEmbarked = sc.nextBoolean();
		} catch (java.util.InputMismatchException ime){
			ime.printStackTrace();
		}
			
		updatePassengerState.setBoolean(1, isEmbarked);
		updatePassengerState.setString(2, passportID);
		updatePassengerState.setString(3, flightID);
		
		try {		
			updatePassengerState.executeUpdate();	
		} catch (org.postgresql.util.PSQLException e){
			e.printStackTrace();
		} finally {
			updatePassengerState.close();
		}
		
		
		
		//Close connection
		con.close();
	
		System.out.println("I am disconneted from the database.");
	    }    
}
