package oddrunp;

import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.Scanner;

import com.mysql.jdbc.PreparedStatement;

public class Dagbok {
	Connection myConn;
	Statement myStmt;
	ResultSet myRs;
	Scanner inint = new Scanner(System.in);
	Scanner instring = new Scanner(System.in);
	Scanner in3 = new Scanner(System.in);
	Scanner in4 = new Scanner(System.in);
	
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
	

	public int setTreningId() throws SQLException {

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
	
	public int setOvelseId() throws SQLException {

		query("select max(OID) from OVELSE");
		int id = 0;
		
		while (myRs.next()) {
			String s = myRs.getString(1);
			
			int id2 = Integer.parseInt(s);
			id += id2 + 1;
		}
		return id;
	}
	
	public void setTrening() throws SQLException, ParseException{
		String query1 = "INSERT INTO TRENINGSOKT (TRENINGSOKTID, DATO, TIDSPUNKT, VARIGHET, LUFTKVALITET, ANTALL_TILSKUERE, STEDSTYPE, VAERFORHOLD)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		java.sql.PreparedStatement prepStmt = myConn.prepareStatement(query1);
		String luft = ""; 
		String vaer = "";
		int tilskuer = 0;
		
		
		System.out.println("Sett dato (YYYY-MM-DD): ");
		String datoIn = instring.next();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(datoIn);
        java.sql.Date sqlDato = new java.sql.Date(parsed.getTime());
	    
		System.out.println("Sett tidspunkt (HH:MM:SS) : ");
		String tidIn = instring.next();
		SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        long parsed2 = format2.parse(tidIn).getTime();
        java.sql.Time sqlTid = new java.sql.Time(parsed2);
		
        System.out.println("Trente du inne eller ute? ");
		String sted = instring.next();
		
		if(sted.equalsIgnoreCase("inne")) {
			System.out.println("Hvor mange tilskuere var der? ");
			tilskuer += inint.nextInt();
			System.out.println("Hvordan var luftkvaliteten? ");
			luft += instring.next();
			
			prepStmt.setInt(6, tilskuer);
			prepStmt.setString(5, luft);
		}
	
		if(sted.equalsIgnoreCase("ute")) {
			System.out.println("Hvordan var vaeret? ");
			vaer += instring.next();
			
			prepStmt.setString(8, vaer);
		}
        
		System.out.println("Hvor lenge trente du (I hele timer)?");
		int varighet = inint.nextInt();
		
		
		prepStmt.setInt(1, setTreningId());
		prepStmt.setDate(2, sqlDato);
		prepStmt.setTime(3, sqlTid);
		prepStmt.setInt(4, varighet);
		prepStmt.setString(5, luft);
		prepStmt.setInt(6, tilskuer);
		prepStmt.setString(7, sted);
		prepStmt.setString(8, vaer);

		
		prepStmt.executeUpdate();
		System.out.println("Treningsøkt lagt til!");
	}



	public void setMal() throws SQLException{
		while (true) {
			int ovnr = 0;
				
			//Printer ut alle ovelse som kan velges
			query("select * from OVELSE");
		    System.out.println("Velg �velse:");
			while (myRs.next()){
				System.out.println(" - " + myRs.getString("OVELSESTITTEL"));
			}
				
		    String ovelse = instring.next();
		        
		    //Sjekker at brukeren velger en eksisterende ovelse
		    query("SELECT COUNT(*) AS total FROM OVELSE WHERE OVELSESTITTEL ='" + ovelse + "'");
		        
		    while (myRs.next()){
		        ovnr = myRs.getInt("total");
		    }
		        
		    if (ovnr == 0){
		        System.out.println("Den �velsen finnes ikke i databasen, legg til �velse f�rst.");
		        break;
		    }
		       
		    System.out.println("Skriv inn m�let for ovelsen:");
		    String mal = in4.nextLine();
		        
		
		    query("SELECT * FROM OVELSE WHERE OVELSESTITTEL ='" + ovelse + "'");
		        
			    while (myRs.next()){
			        
			        	//Kode for hvis det ikke finnes et mal
			    	if (myRs.getString("GOAL") == null){
			        	
			        	//Hvis det allerede finnes et mal, skal det legges til i malhistorikk
			        	String updategoal = "UPDATE OVELSE SET GOAL = ? WHERE OVELSESTITTEL = ?";
			        	java.sql.PreparedStatement stmt = myConn.prepareStatement(updategoal);
			        	
			        	stmt.setString(1, mal);
			        	stmt.setString(2, ovelse);
			            
			            stmt.executeUpdate();
			        	
			        	System.out.println("Mal er lagt til!");
			        	
			        	System.out.println("Mal for ovelse er oppdatert!");
			        	query("SELECT * FROM OVELSE WHERE OVELSESTITTEL ='" + ovelse + "'");
			        	while (myRs.next()){
			        	System.out.println("Ovelse: " + myRs.getString("OVELSESTITTEL") + " - Mal: " + myRs.getString("GOAL"));
			        
			        }
		       }
		        
		        //Kode for hvis det finnes et mal fra for av
		        else{
		
		        //Malet blir lagt til i malhistorikk
		        String query1 = "INSERT INTO MALHISTORIKK (OVELSENAVN, MAL)" + "VALUES (?, ?)";
		        java.sql.PreparedStatement prepStmt = myConn.prepareStatement(query1);
		        
		        prepStmt.setString(1, myRs.getString("OVELSESTITTEL"));	
		        prepStmt.setString(2, myRs.getString("GOAL"));
		        
		        prepStmt.executeUpdate();
		        
		        
		        System.out.println("Det gamle malet ble lagt til malhistorikken");
		        
		        //Det nye malet blir lagt til i ovelsetabellen
		        String updategoal = "UPDATE OVELSE SET GOAL = ? WHERE OVELSESTITTEL = ?";
		    	java.sql.PreparedStatement stmt = myConn.prepareStatement(updategoal);
		    	
		    	stmt.setString(1, mal);
		    	stmt.setString(2, ovelse);
		        
		        stmt.executeUpdate();
		      
		        System.out.println("Malet for ovelse er oppdatert");
		        query("SELECT * FROM OVELSE WHERE OVELSESTITTEL ='" + ovelse + "'");
		        while (myRs.next()){
		    	System.out.println("Ovelse: " + myRs.getString("OVELSESTITTEL") + " - Mal: " + myRs.getString("GOAL"));
		    
		        	}
		        }  
		        
			}
		        System.out.println("For � legge til et nytt mal, tast 1. For � avslutte, tast et annet siffer");
		        int fortsett = inint.nextInt();
		        if(fortsett == 1){
		        	continue;
		        }
		        break;
			}
			
	}

	public void getMalhistorikk() throws SQLException{
		query("SELECT * FROM MALHISTORIKK");
		while(myRs.next()){
		System.out.println("Ovelse: " + myRs.getString("OVELSENAVN") + " - Mal: " + myRs.getString("MAL"));
	}
}


	public void setOvelse() throws SQLException{
		
		String query1 = "INSERT INTO OVELSE (OID, OVELSESTITTEL, PERSFORM, BESKRIVELSE, TYPE_TRENING, BELASTNING, ANTALL_REPETISJONER, ANTALL_SETT, VARIGHET, OVELSEID, GOAL, RESULTAT, NOTAT)" + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		java.sql.PreparedStatement stat1 = myConn.prepareStatement(query1);
		
		//query("select TRENINGSOKTID from TRENINGSOKT);"
		//String treningsoktid = 
		System.out.println("Skriv inn Øvelsetittel: ");
		String tittel = instring.next();
		System.out.println("Skriv inn personlig form: ");
		int persForm = inint.nextInt();
		System.out.println("Skriv inn beskrivelse av økten: ");
		String beskrivelse = in3.nextLine();
		System.out.println("Skriv inn type trening her: ");
		String typeTrening = in3.next();
		System.out.println("Grad av belastning: ");
		String belastning = instring.next();
		System.out.println("Skriv inn antall repetisjoner: ");
		int ant_rep = inint.nextInt();
		System.out.println("Skriv inn antall sett: ");
		int ant_set = inint.nextInt();
		System.out.println("Varigheten for øvelsen (i timer): ");
		float varighet = inint.nextFloat();
		System.out.println("Skriv inn resultat: ");
		String resultat = in4.nextLine();
		System.out.println("Skriv inn notat / tips for øvelsen: ");
		String notat = in4.nextLine();
		System.out.println("Øvelse lagt til!");
	
		
		stat1.setInt(1, setOvelseId());
		stat1.setString(2,tittel);
		stat1.setInt(3, persForm);
		stat1.setString(4, beskrivelse);
		stat1.setString(5, typeTrening);
		stat1.setString(6, belastning);
		stat1.setInt(7, ant_rep);
		stat1.setInt(8, ant_set);
		stat1.setFloat(9, varighet);
		stat1.setInt(10, getLastId());
		stat1.setString(11, null);
		stat1.setString(12, resultat);
		stat1.setString(13, notat);
		
		stat1.executeUpdate();
		
		}




	public void getOvelse()throws SQLException{
		query("select * from OVELSE");
		System.out.println("Dette er alle de tidligere øvelsene: ");
		//Når myResult har en neste, print ut OVELSESTITTEL,BESKRIVELSE og TYPE_TRENING
		while (myRs.next()) {
			System.out.println("Øvelsestittel: " + myRs.getString("OVELSESTITTEL"));
		}
		
		System.out.println(' ');
		System.out.println("Hvilken øvelse vil du hente ut? (velg fra øvelsestitler i listen over): ");
		String valgt = instring.next();
		query("select * from OVELSE where OVELSESTITTEL ='" + valgt + "'");

		while (myRs.next()){
		System.out.println("Øvelsestittel: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING")+ "Belastning: " + myRs.getString("BELASTNING") + "Antall Repetisjoner: " + myRs.getString("ANTALL_REPETISJONER") + "Antall Sett: " + myRs.getString("ANTALL_SETT") + "Varighet: " + myRs.getString("VARIGHET") + "Mål: " + myRs.getString("GOAL") + "Resultat: " + myRs.getString("RESULTAT") + "Notat: " + myRs.getString("NOTAT"));
		}
		/*
		 * Lager statements som matcher øvelseNavn i database
		 */
	}

	public void getTrening() throws SQLException {
		System.out.println("Velg dato: ");
		query("select * from TRENINGSOKT");
		while (myRs.next()){
			System.out.println(" - " + myRs.getString("DATO"));
		}
		
		while (true) {
		String DT = instring.next();
	    int dnr = 0;    
	    //Sjekker at brukeren velger en eksisterende ovelse
	    query("SELECT COUNT(*) AS total FROM TRENINGSOKT WHERE DATO ='" + DT + "'");   
	    while (myRs.next()){
	        dnr = myRs.getInt("total");
	    }   
	    if (dnr == 0){
	        System.out.println("Den datoen finnes ikke i databasen, pr�v igjen:");
	        continue;
	    }
		
		
		
		query("SELECT * FROM TRENINGSOKT AS T INNER JOIN OVELSE AS O ON (T.TRENINGSOKTID = O.OVELSEID) WHERE DATO='" + DT + "' AND (O.OVELSEID = T.TRENINGSOKTID)");

		if(myRs.next()) {
			System.out.println("Treningsokt id" + myRs.getString("TRENINGSOKTID") + "  Dato: " + myRs.getString("DATO") + "  Tidspunkt: " + myRs.getString("TIDSPUNKT") + "  Varighet: " + myRs.getString("VARIGHET"));
			System.out.println("Øvelseid:" + myRs.getString("OVELSEID") + " Øvelse navn: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING"));

		}
		while (myRs.next()) {
			System.out.println(" Øvelse navn: " + myRs.getString("OVELSESTITTEL") + "  Beskrivelse: " + myRs.getString("BESKRIVELSE") + "  Type trening: " + myRs.getString("TYPE_TRENING"));
			
		}
		break;
	}
}
	
	
	public static void main(String[] args) throws SQLException, ParseException {
		Dagbok d = new Dagbok();
		d.setMal();
	}
}
