package database;

import java.sql.*;

public class Driver {
	
	public static void main(String[] args) {
		try {
			Connection myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/yntran_tdt4145", "yntran_tdt4145",  "databasegruppe2");
		
			Statement myStmt = myConn.createStatement();
			
			ResultSet myRs = myStmt.executeQuery("select * from PERSON");
			
			while (myRs.next()) {
				System.out.println(myRs.getString("PERSONID") + ", " + myRs.getString("PERSONLIGFORM"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}