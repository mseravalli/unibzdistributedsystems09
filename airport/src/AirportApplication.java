import java.sql.*;
import java.io.*;

public class AirportApplication extends PostgreSqlAccess{

    public static void main(String args[]) throws Exception {

	//load the driver 
	Class.forName(driverName);

	//establish a connection to the database via the driver
	Connection con = DriverManager.getConnection(dburl, user, passwd);
	
	System.out.println("I am connected to the database " + dburl + ".");

	//Close connection
	con.close();

	System.out.println("I am disconneted from the database.");
    }    
}
