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
		
			
			
			String updatePassengerPattern = "BEGIN TRANSACTION;" +
					"UPDATE daily_flight SET departure_date = ? " +
					"WHERE flight_id = ? AND departure_date = ?;" +
					"COMMIT WORK;";
	
			PreparedStatement updatePassengerState = con.prepareStatement(updatePassengerPattern);
			
			String oldDate = "2009-11-19 15:35:00";
			String flightID = "BA9376";
			String newDateString = "2009-11-19 17:35:00";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			//Date newDate = df.parse("2009-11-19 17:35:00");
			Date newDate = new Date(df.parse("2009-11-19 17:35:00").getTime());
			
			
			updatePassengerState.setString(1, oldDate);
			updatePassengerState.setString(2, flightID);
			updatePassengerState.setDate(3, newDate);
			
			updatePassengerState.executeUpdate();
			
			String selectQuery =
			    "SELECT   * " +
			    "FROM     trip " +
			    "WHERE    flight_id = '"+flightID+"' AND departure_date = '"+oldDate+"';";
			
			Statement stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(selectQuery);

			changeTrips(rs, con, newDate.toString());
			changeCIPAssignement(rs, con, newDate.toString());
			
			con.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void changeTrips(ResultSet rs, Connection con, String newDate) {
	    try{
	    	
	    	rs.next();
	    	
	    	String flightID = rs.getString(1);
	    	Date oldDate = rs.getDate(2);
	    	
	    	String selectQuery =
			    "UPDATE trip" +
			    "SET 	departure_date = '" + newDate + "' " +
			    "WHERE	flight_id = '"+flightID+"' AND departure_date = '"+oldDate+"';";
			
			Statement stmt = con.createStatement();
			stmt.executeQuery(selectQuery);
	    	
	        rs.close();
	    //Catch exceptions
	    }catch (Exception e) {
	         screen.println(e.toString());}
	}

	
	public static void changeCIPAssignement(ResultSet rs, Connection con, String newDate) {
	    try{
	    	
	    	rs.next();
	    	
	    	String flightID = rs.getString(1);
	    	String oldDate = rs.getString(2);
	    	
	    	String selectQuery =
			    "UPDATE cip_assignment" +
			    "SET 	departure_date = '" + newDate + "' " +
			    "WHERE	flight_id = '"+flightID+"' AND departure_date = '"+oldDate+"';";
			
			Statement stmt = con.createStatement();
			stmt.executeQuery(selectQuery);
	    	
	        rs.close();
	    //Catch exceptions
	    }catch (Exception e) {
	         screen.println(e.toString());}
	}
	

    public static void main(String args[]) throws Exception {
    	
    	
    	updateFlyingTime();
    	
    	
    }
/*
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
