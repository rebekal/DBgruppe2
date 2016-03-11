package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	//Lager disse globale variablene for å kunne bruke de i metodene.
	//Må være konsekvent
	Connection myConn;
	Statement myStmt;
	ResultSet myRs;
	
	public Test(){
		
		try {
			//Starter connection her: url, brukernavn, passord
			Connection myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/yntran_tdt4145", "yntran_tdt4145",  "databasegruppe2");
			this.myConn = myConn;
			System.out.println("Database connected!");
			
			//Lager statement... Har ikke peiling på hvorfor jeg har dette her.
			//Kan evt flytte dette til query
			Statement myStmt = myConn.createStatement();
			this.myStmt = myStmt;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void query(String sql) {
		try{
			/*
			 * Siden spørringer blir gjentatt mange ganger, så kan vi gjenbruke 
			 * denne koden ved å skrive spørringen 
			 */
			ResultSet myRs = myStmt.executeQuery(sql);
			this.myRs = myRs;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public void getPerson() throws SQLException {
		//lager en spørring
		query("select * from PERSON");
		//Når myResult har en neste, print ut PERSINID og PERSONLIGFORM
		while (myRs.next()) {
			System.out.println(myRs.getString("PERSONID") + ", " + myRs.getString("PERSONLIGFORM"));
		}
	}
	public static void main(String[] args) throws SQLException {
		//Lager ny test object
		Test t = new Test();
		t.getPerson();
		
	}
}