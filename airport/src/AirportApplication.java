import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.*;

public class AirportApplication extends PostgreSqlAccess{
	
	
	public static void updateFlyingTime(){
		try {
			
			
			String oldDate = "2009-11-19 17:35:00";
			String flightID = "BA9376";
			String newDateString = "2009-11-19 15:35:00";
			
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("Insert the flight id");
			//flightID = sc.nextLine();
			
			System.out.println("Insert the departure date of the flight");
			//oldDate = sc.nextLine();
			
			System.out.println("Insert the new departure date of the flight");
			//newDateString = sc.nextLine();
			

			Class.forName(driverName);
		
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			//System.out.println("I am connected to the database " + dburl + ".");			
			
			String selectQuery =
				"BEGIN TRANSACTION;" +
				"UPDATE daily_flight SET departure_date = '" + newDateString + "' " +
				"WHERE flight_id = '" + flightID + "' AND departure_date = '" + oldDate + "';" +
				"COMMIT WORK;";
			
			Statement stmt = con.createStatement();
			int i = stmt.executeUpdate(selectQuery);
			
			System.out.println("query executed");
			
			stmt.close();
			con.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	public static void tripForAPassenger(){
		
		try {
			
			
			Scanner sc = new Scanner(System.in);
			
			String passportID = "152347867";
			String birthDate = "1993-06-14";
			String name = "Javier";
			String surname = "Casorla";
			String address = "SP,Cervera, Carrer de Napols 7";
			
			String flightID = "U27309";
			String departureDate = "2009-11-20 01:35:00";
			String bookingDate = "2009-09-27";
			String cipNo = "NULL";
			String embarked = "FALSE";
			
			System.out.println("Insert the passport id");
			//passportID = sc.nextLine();
			
			System.out.println("Insert the birth date");
			//birthDate = sc.nextLine();
			
			System.out.println("Insert the name");
			//name = sc.nextLine();
			
			System.out.println("Insert the surname");
			//surname = sc.nextLine();
			
			System.out.println("Insert the address");
			//address = sc.nextLine();
			
			
			Class.forName(driverName);
		
		
		
			//establish a connection to the database via the driver
			Connection con = DriverManager.getConnection(dburl, user, passwd);
			
			//System.out.println("I am connected to the database " + dburl + ".");
			
			//firstly a passenger is created
			String createPassenger = "BEGIN TRANSACTION;" +
					"INSERT INTO passenger (passport_ID,birthdate,name,surname,address) " +
					"VALUES ('" + passportID + "','" + birthDate + "','" + name + 
					"','" + surname + "','" + address + "');" +
					"SAVEPOINT passenger_created;";
			
			Statement stmt = con.createStatement();
			
			System.out.println("passenger created");
			
			System.out.println("Insert the flight id");
			//flightID = sc.nextLine();
			System.out.println("Insert the departure date");
			//departureDate = sc.nextLine();
			System.out.println("Insert the booking date");
			//bookingDate = sc.nextLine();
			
			stmt.executeUpdate(createPassenger);
			
			String createTrip = "INSERT INTO trip (flight_ID, departure_date, passport_ID, booking_date, cip_no,embarked) " +
					"VALUES ('"+flightID+"','"+departureDate+"','"+passportID+"','"+bookingDate+"',"+cipNo+" ,"+embarked+");" +
					"rollback to savepoint passenger_created;";
					//"COMMIT WORK;";
			
			//System.out.println(createTrip);
			
			stmt.executeUpdate(createTrip);
			
			System.out.println("query executed");
			
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
			
			//System.out.println("I am connected to the database " + dburl + ".");
		
			String updatePassengerPattern = "BEGIN TRANSACTION;" +
					"SET TRANSACTION ISOLATION LEVEL READ COMMITTED;" +
					"UPDATE trip SET embarked = ? WHERE passport_id = ? AND flight_id = ?;" +
					"COMMIT WORK;";
			
			PreparedStatement updatePassengerState = con.prepareStatement(updatePassengerPattern);
			
			String passportID = "9873648";
			String flightID = "AF6132";
			boolean isEmbarked = false;
			
			Scanner sc = new Scanner(System.in);
			
			System.out.println("insert passport, flight id and if that passenger is" +
					"actually embarked");
		
		
			
			System.out.print("passport id: ");
			//passportID = sc.nextLine();
			
			System.out.print("flight id: ");
			//flightID = sc.nextLine();
			
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
    	
    	Scanner sc =  new Scanner(System.in);
    	
    	int selection = 0;
    	
    	String instructions = "instructions:\n" +
    			"0 - exit\n" +
    			"1 - instructions\n" +
    			"2 - update the time of a daily flight\n" +
    			"3 - create a passenger and a trip for (s)he\n" +
    			"4 - embark a passenger\n";
    	
    	System.out.println(instructions);
    	
    	do{
    		try{
    			
    			System.out.print("\ntype your selection:\t");
    			selection = sc.nextInt();
    			
    			switch(selection){
	    			case 0:{
						System.out.println("exit");
						break;
					}
    				case 1:{
    					System.out.println(instructions);
    					break;
    				}
    				case 2:{
    					updateFlyingTime();
    					break;
    				}
    				case 3:{
    					tripForAPassenger();
    					break;
    				}
					case 4:{
						embarkPassenger();
    					break;
    				}
					default:{
						System.out.println("invalid selection");
						break;
					}
    				
    			}
    			
    			
    			
    		} catch(java.util.InputMismatchException e) {
    			e.printStackTrace();
    		}
    		
    	}while(selection != 0);
    	
    	
    }

}
