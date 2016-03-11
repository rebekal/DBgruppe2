package database;

import java.sql.*;

public class Main {

	public static void main(String[] args){
		try{
			Connection myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/test", "yntran_tdt4145", "databasegruppe2");
			Statement myStmt = myConn.createStatement();
			ResultSet myRs = myStmt.executeQuery("select * from Ovelse");
			while (myRs.next()){
				System.out.println(myRs.getString("navn")+"," + myRs.getString("beskrivelse"));
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
