package oddrunp;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class Main extends Dagbok {
	
	public static void main(String[] args) throws SQLException, ParseException {
		Dagbok dagbok = new Dagbok();
		boolean fortsett = true;
		Scanner input = new Scanner (System.in);
		
		System.out.println("\nVelkommen til din personlige treningsdagbok." + "\n\nKommandonummer: " +
							"\n1 : Sett trening" + 
							"\n2 : Hent trening" +
							"\n3 : Legg til øvelse" +
							"\n4 : Hent øvelse" + 
							"\n5 : Sett mål for øvelse" +
							"\n6 : Hent målhistorikk"+
							"\n0 : Exit");
		
		while (fortsett == true) {
			System.out.println("\nSkriv kommandonummer her:");
			String in = input.nextLine();
			
			switch (in) {
				case "1":
					dagbok.setTrening();
					dagbok.setOvelse();
					while (true){
						System.out.println("Trykk 1 for aa legge til flere ovelser");
						System.out.println("Trykk hvilket som helst annet siffer for aa fortsette");
						int newovelse = input.nextInt();
					if(newovelse == 1){
						dagbok.setOvelse();
					}
					else{
						break;
					}
					}
					continue;
					
				case "2":
					dagbok.getTrening();
					continue;
				
				case "3":
					dagbok.setOvelse();
					continue;
					
				case "4":
					dagbok.getOvelse();
					continue;
				
				case "5":
					dagbok.setMal();
					continue;
				
				case "6":
					dagbok.getMalhistorikk();
					continue;
				case "0":
					System.out.println("du er naa ute av programmet");
					fortsett = false;
				break;
				
				default:
					System.out.println("Kommandonummeret finnes ikke, skriv en fra menyen");
					continue;
				
			}
		}
	}
}
