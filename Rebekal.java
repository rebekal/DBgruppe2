package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class Rebekal {
	
	Connection myConn;
	Statement myStmt;
	ResultSet myRs;
	
	public Rebekal(){
			
			try {
				//Starter connection her: url, brukernavn, passord
				Connection myConn = DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/yntran_tdt4145", "yntran_tdt4145", "databasegruppe2");
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

	public void setOvelse(){
		System.out.println("Skriv inn �velsetittel: ");
		Scanner scanner= new Scanner(System.in);
		String tittel = scanner.next();
		System.out.println("Skriv inn beskrivelse av �kten: ");
		String beskrivelse = scanner.next();
		System.out.println("Skriv inn type trening her: ");
		String typeTrening = scanner.next();
		scanner.close();
	}
	
	public void getOvelse(String ovelseNavn){
		query("select * from Ovelse");
		/*
		 * Lager statements som matcher �velseNavn i database
		 */
	}
	
	public String deleteOvelse(String ovelse){
		System.out.println("�nsker du � slette en �velse? (J/N) ");
		Scanner scanner= new Scanner(System.in);
		if (scanner.next().equalsIgnoreCase("ja")){
			System.out.println("Skriv inn tittel p� �velsen");
			if (scanner.next().equals(getOvelse.tittel())){
				//slett rad i SQL
		}
			
		}
	}
	
	public void setMal(){
		System.out.println("�nsker du � bruke en tidligere trenigns�kt mal? (J/N) ");
		Scanner scanner= new Scanner(System.in);
		if (scanner.next().equalsIgnoreCase("ja")){
			trenings�ktID == getTrening();
		}	
		}
	
	public String getMal(String mal){
		return mal;
	}
	
	public static void main(String[] args) {
		Rebekal r = new Rebekal();
		r.setOvelse();
		r.getOvelse();
		r.setMal();
		r.getMal();
		r.deleteOvelse();
		
	}
	
	
}
