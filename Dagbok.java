package database;

import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.Scanner;

public class Dagbok {
	Connection myConn;
	Statement myStmt;
	ResultSet myRs;
	Scanner in = new Scanner(System.in);
	
	public Dagbok(){
		
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
	



	public void setTrening() throws SQLException, ParseException{
		String query1 = "INSERT INTO TRENINGSOKT (TRENINGSOKTID, DATO, TIDSPUNKT, VARIGHET)" + "VALUES (?, ?, ?, ?)";
		PreparedStatement prepStmt = myConn.prepareStatement(query1);
		
		System.out.println("Skriv inn treningsøktid: ");
		int treningsoktid = in.nextInt();
		
		System.out.println("Sett dato (YYYY-MM-DD): ");
		String datoIn = in.next();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(datoIn);
        java.sql.Date sqlDato = new java.sql.Date(parsed.getTime());
	    
		System.out.println("Sett tidspunkt (HH:MM:SS) : ");
		String tidIn = in.next();
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        long parsed2 = format2.parse(tidIn).getTime();
        java.sql.Time sqlTid = new java.sql.Time(parsed2);
		
		
		System.out.println("Hvor lenge trente du (I hele timer)?");
		int varighet = in.nextInt();
		
		
		prepStmt.setInt(1, treningsoktid);
		prepStmt.setDate(2, sqlDato);
		prepStmt.setTime(3, sqlTid);
		prepStmt.setInt(4, varighet);
		
		prepStmt.executeUpdate();
		System.out.println("Treningsøkt lagt til!");
	}




	public void getTrening() throws SQLException {
			query("select * from TRENINGSOKT");
			//Når myResult har en neste, print ut PERSINID og PERSONLIGFORM
			while (myRs.next()) {
				System.out.println("TreningsID: " + myRs.getString("TRENINGSOKTID") + "  Dato: " + myRs.getString("DATO") + "  Tidspunkt: " + myRs.getString("TIDSPUNKT") + "  Varighet: " + myRs.getString("VARIGHET"));
			}
		}




	public void setOvelse() throws SQLException{
		
		String query1 = "INSERT INTO OVELSE (OVELSESTITTEL, BESKRIVELSE, TYPE_TRENING)" + "VALUES (?,?,?)";
		PreparedStatement stat1 = myConn.prepareStatement(query1);
		
		
		System.out.println("Skriv inn Øvelsetittel: ");
		String tittel = in.next();
		System.out.println("Skriv inn beskrivelse av økten: ");
		String beskrivelse = in.next();
		System.out.println("Skriv inn type trening her: ");
		String typeTrening = in.next();
		System.out.println("Øvelse lagt til!");
		
	
		stat1.setString(1,tittel);
		stat1.setString(2, beskrivelse);
		stat1.setString(3, typeTrening);
		
		stat1.executeUpdate();
		
		}




	public void getOvelse()throws SQLException{
		query("select * from OVELSE");
		//Når myResult har en neste, print ut OVELSESTITTEL,BESKRIVELSE og TYPE_TRENING
		while (myRs.next()) {
			System.out.println("Øvelsestittel: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING"));
		}


	
	
	public static void main(String[] args) throws SQLException, ParseException {
		Dagbok d = new Dagbok();
		d.setTrening();
		d.getTrening();
		d.setOvelse();
	}
}
