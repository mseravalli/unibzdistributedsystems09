import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.*;

public class AirportApplication extends PostgreSqlAccess{
	
	
	public static void updateFlyingTime(){
		try {
			Class.forName(driverName);
		
		
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			System.out.println("I am connected to the database " + dburl + ".");
			
			String oldDate = "2009-11-19 17:35:00";
			String flightID = "BA9376";
			String newDateString = "2009-11-19 15:35:00";			
			
			String selectQuery1 =
				"BEGIN TRANSACTION;" +
				"UPDATE daily_flight SET departure_date = '"+newDateString+"' " +
				"WHERE flight_id = '"+flightID+"' AND departure_date = '"+oldDate+"';" +
				"COMMIT WORK;";
			
			Statement stmt1 = con.createStatement();
			stmt1.executeUpdate(selectQuery1);
			
			con.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	public static void tripForAPassenger(){
		
		try {
			Class.forName(driverName);
		
		
		
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			System.out.println("I am connected to the database " + dburl + ".");
			
			//firstly a passenger is created
			String createPassenger = "BEGIN TRANSACTION;" +
					"INSERT INTO passenger (passport_ID,birthdate,name,surname,address) " +
					"VALUES ('152347867','1990-06-14','Javier','Casorla','SP,Cervera, Carrer de Napols 7');" +
					"SAVEPOINT passenger_created;";
			
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate(createPassenger);
			
			String createTrip = " INSERT INTO trip (flight_ID,departure_date,passport_ID,booking_date,cip_no,embarked) " +
					"VALUES ('U27309','2009-11-20 01:35:00','152347867','2009-09-27',4,FALSE);" +
					"rollback to savepoint passenger_created;";
			
			stmt.executeUpdate(createTrip);
			
			stmt.close();
			con.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void embarkPassenger(){
		
		try{
		
			Class.forName(driverName);
			
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			System.out.println("I am connected to the database " + dburl + ".");
		
			String updatePassengerPattern = "BEGIN TRANSACTION;" +
					"SET TRANSACTION ISOLATION LEVEL READ COMMITTED;" +
					"UPDATE trip SET embarked = ? WHERE passport_id = ? AND flight_id = ?;" +
					"COMMIT WORK;";
			
			PreparedStatement updatePassengerState = con.prepareStatement(updatePassengerPattern);
			
			String passportID = "";
			String flightID = "";
			boolean isEmbarked = false;
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("insert passport, flight and if that passenger is" +
					"actually embarked");
		
		
			
			System.out.print("passport id: ");
			passportID = "9873648";
			
			System.out.print("flight id: ");
			flightID = "AF6132";
			
			System.out.print("is embarked: ");
			isEmbarked = sc.nextBoolean();
			
			updatePassengerState.setBoolean(1, isEmbarked);
			updatePassengerState.setString(2, passportID);
			updatePassengerState.setString(3, flightID);
		
			updatePassengerState.executeUpdate();	
			
			updatePassengerState.close();
			
			con.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}	
		
		//Close connection

	
		System.out.println("I am disconneted from the database.");
	    
	}
	
	

    public static void main(String args[]) throws Exception {
    	
    	
    	updateFlyingTime();
    	
    	
    }
/*
 * 
 *  INSERT INTO trip (flight_ID,departure_date,passport_ID,booking_date,cip_no,embarked) VALUES ('U27309','2009-11-20 01:35:00','152647867','2009-09-27',4,FALSE);
 * 
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
	    */
}
