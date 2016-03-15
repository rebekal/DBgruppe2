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
	

	public int setId() throws SQLException {

		query("select max(TRENINGSOKTID) from TRENINGSOKT");
		int id = 0;
		
		while (myRs.next()) {
			String s = myRs.getString(1);
			
			int id2 = Integer.parseInt(s);
			id += id2 + 1;
		}
		return id;
	}

	public int getLastId() throws SQLException {

		query("select max(TRENINGSOKTID) from TRENINGSOKT");
		int id = 0;
		
		while (myRs.next()) {
			String s = myRs.getString(1);
			
			int id2 = Integer.parseInt(s);
			id += id2;
		}
		return id;
	}
	
	public void setTrening() throws SQLException, ParseException{
		String query1 = "INSERT INTO TRENINGSOKT (TRENINGSOKTID, DATO, TIDSPUNKT, VARIGHET)" + "VALUES (?, ?, ?, ?)";
		PreparedStatement prepStmt = myConn.prepareStatement(query1);
		
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
		
		
		prepStmt.setInt(1, setId());
		prepStmt.setDate(2, sqlDato);
		prepStmt.setTime(3, sqlTid);
		prepStmt.setInt(4, varighet);
		
		prepStmt.executeUpdate();
		System.out.println("Treningsøkt lagt til!");
	}





	public void setOvelse() throws SQLException{
		
		String query1 = "INSERT INTO OVELSE (OVELSESTITTEL, PERSFORM, BESKRIVELSE, TYPE_TRENING, BELASTNING, ANTALL_REPETISJONER, ANTALL_SETT, VARIGHET, OVELSEID)" + "VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement stat1 = myConn.prepareStatement(query1);
		
		//query("select TRENINGSOKTID from TRENINGSOKT);"
		//String treningsoktid = 
		System.out.println("Skriv inn Øvelsetittel: ");
		String tittel = in.next();
		System.out.println("Skriv inn personlig form: ");
		String persForm = in.next();
		System.out.println("Skriv inn beskrivelse av økten: ");
		String beskrivelse = in.next();
		System.out.println("Skriv inn type trening her: ");
		String typeTrening = in.next();
		System.out.println("Grad av belastning: ");
		String belastning = in.next();
		System.out.println("Skriv inn antall repetisjoner: ");
		String ant_rep = in.next();
		System.out.println("Skriv inn antall sett: ");
		String ant_set = in.next();
		System.out.println("Skriv inn varigheten for øvelsen: ");
		String varighet = in.next();
		System.out.println("Øvelse lagt til!");
		
	
		stat1.setString(1,tittel);
		stat1.setString(2, persForm);
		stat1.setString(3, beskrivelse);
		stat1.setString(4, typeTrening);
		stat1.setString(5, belastning);
		stat1.setString(6, ant_rep);
		stat1.setString(7, ant_set);
		stat1.setString(8, varighet);
		stat1.setInt(9, getLastId());
		
		stat1.executeUpdate();
		
		}




	public void getOvelse()throws SQLException{
		query("select * from OVELSE");
		System.out.println("Dette er alle de tidligere øvelsene: ");
		//Når myResult har en neste, print ut OVELSESTITTEL,BESKRIVELSE og TYPE_TRENING
		while (myRs.next()) {
			System.out.println("Øvelsestittel: " + myRs.getString("OVELSESTITTEL") + "Personlig Form: " + myRs.getString("PERSFORM") +  "Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING")+ "Belastning: " + myRs.getString("BELASTNING") + "Antall Repetisjoner: " + myRs.getString("ANTALL_REPETISJONER") + "Antall Sett: " + myRs.getString("ANTALL_SETT") + "Varighet: " + myRs.getString("VARIGHET"));
		}
		
		System.out.println(' ');
		System.out.println("Hvilken øvelse vil du hente ut? (velg fra øvelsestitler i listen over): ");
		String valgt = in.next();
		query("select OVELSESTITTEL, PERSFORM, BESKRIVELSE, TYPE_TRENING, BELASTNING, ANTALL_REPETISJONER, ANTALL_SETT, VARIGHET  from OVELSE where OVELSESTITTEL ='" + valgt + "'");
		while (myRs.next()){
		System.out.println("Øvelsestittel: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING")+ "Belastning: " + myRs.getString("BELASTNING") + "Antall Repetisjoner: " + myRs.getString("ANTALL_REPETISJONER") + "Antall Sett: " + myRs.getString("ANTALL_SETT") + "Varighet: " + myRs.getString("VARIGHET"));
		}
		/*
		 * Lager statements som matcher øvelseNavn i database
		 */
	}
	
	public void getTrening() throws SQLException {
		System.out.println("Skriv inn dato ");
		String DT = in.next();
		query("SELECT * FROM TRENINGSOKT AS T INNER JOIN OVELSE AS O ON (T.TRENINGSOKTID = O.OVELSEID) WHERE DATO='" + DT + "' AND (O.OVELSEID = T.TRENINGSOKTID)");

		if(myRs.next()) {
			System.out.println("Treningsokt id" + myRs.getString("TRENINGSOKTID") + "  Dato: " + myRs.getString("DATO") + "  Tidspunkt: " + myRs.getString("TIDSPUNKT") + "  Varighet: " + myRs.getString("VARIGHET"));
			System.out.println("Øvelseid:" + myRs.getString("OVELSEID") + " Øvelse navn: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING"));

		}
		while (myRs.next()) {
			System.out.println("Øvelseid:" + myRs.getString("OVELSEID") + " Øvelse navn: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING"));
			
		}
	}
	
	
	public static void main(String[] args) throws SQLException, ParseException {
		Dagbok d = new Dagbok();
		d.setTrening();
		d.setOvelse();
		d.setOvelse();
		d.getTrening();
	}
}
