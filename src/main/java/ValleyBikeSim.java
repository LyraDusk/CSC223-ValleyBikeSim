
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ValleyBikeSim {
	static boolean running = true;
	public static void main(String[] args) {
		//Read in data
		ArrayList<String[]> stationData = new ArrayList<>();
		ArrayList<String[]> rideData = new ArrayList<>();
		ArrayList<String[]> appendedData = new ArrayList<>();
		try {
			BufferedReader stationreader = new BufferedReader(new FileReader("./data-files/station-data.csv"));
			String line = stationreader.readLine();
			
			while(line != null) {
				String[] row = line.split(",");
				stationData.add(row);
				line = stationreader.readLine();
			}
			stationreader.close();
			
		} catch (Exception e) {
			System.out.println("Could not find data file!");
			}
		
		System.out.println("Welcome to ValleyBike Simulator.");
		Scanner scanner = new Scanner(System.in);
		
		
		while(running) {
			
			//Print the options
			System.out.println("0. Quit Program");
			System.out.println("1. View Station List");
			System.out.println("2. Add Station");
			System.out.println("3. Save Station List");
			System.out.println("4. Record Ride");
			System.out.println("5. Resolve Ride Data");
			System.out.println("6. Equalize Stations");
			String input = scanner.next();
			
			//Quit the program
			if(input.equals("0")) {
				quit();
			}
			
			//List stations
			if(input.equals("1")) {
				for(int i = 0; i < stationData.size(); i ++) {
					System.out.println(String.join(" ", stationData.get(i)));
					
				}
				System.out.println("0: Quit Program; 1: Go back.");
				input = scanner.next();
				if(input.equals("0")) {
					quit();
				}
				if(input.equals("1")) {
					continue;
				}
				
			}
			//Add a station
			if(input.equals("2")) {
				System.out.println("ID: ");
				String id = scanner.next();
				System.out.println("Name: ");
				String name = scanner.next();
				System.out.println("Bikes: ");
				String bikes = scanner.next();
				System.out.println("Pedelecs: ");
				String pedelecs = scanner.next();
				System.out.println("Available: ");
				String available = scanner.next();
				System.out.println("Maintainance: ");
				String maintainance = scanner.next();
				System.out.println("Capacity: ");
				String capacity = scanner.next();
				System.out.println("Kiosk: ");
				String kiosk = scanner.next();
				System.out.println("Address: ");
				String address = scanner.next();
				String[] row = {id,name,bikes,pedelecs,available,maintainance,capacity,kiosk,address};
				stationData.add(row);
				appendedData.add(row);
				System.out.println("Added line: " + String.join(", ", row) + ".");
				System.out.println("0: Quit Program; 1: Go back.");
				input = scanner.next();
				if(input.equals("0")) {
					quit();
				}
				if(input.equals("1")) {
					continue;
				}
			}
			
			//Save data
			if(input.equals("3")) {
				try {
					FileWriter writer = new FileWriter("./data-files/station-data.csv");
					for(String[] row: stationData) {
						writer.append(String.join(",", row));
						writer.append("\n");
					}
					writer.close();
				} catch (IOException e) {
					System.out.println("File write error."); 
				}
				
				System.out.println("Changes saved!");
			}
			//Add ride data
			if(input.contentEquals("4")) {
				System.out.println("User ID: ");
				String id = scanner.next();
				System.out.println("From: ");
				String from = scanner.next();
				System.out.println("To: ");
				String to = scanner.next();
				System.out.println("Start: ");
				String start = scanner.next();
				System.out.println("End: ");
				String end = scanner.next();
				String[] ride = {id,from,to,start,end};
				rideData.add(ride);
				
			}
			
			//read ride data
			if(input.equals("5")) {
				System.out.println("File name to read; exact name, with file suffix:");
				try {
				BufferedReader ridereader = new BufferedReader(new FileReader("./data-files/" + input));
				String ride = ridereader.readLine();
				while(ride != null) {
					String[] row = ride.split(",");
					rideData.add(row);
					ride = ridereader.readLine();
					
				}
				ridereader.close();
				} catch(Exception e) {
					System.out.println("File read failed, make sure you spelled the file name correctly!");
				}
			}
			
			//Equalize Stations
			if(input.equals("6")) {
				ArrayList<Double> capacities = new ArrayList<>();
				int i = 1;
				boolean checkEqual = false;
				
				equal_loop:
					//While the stations are not at equal percentage
				while(!checkEqual) {
					boolean skippedFirstRow = false;
				for(String[] row: stationData) {
					if(!skippedFirstRow) {
						skippedFirstRow = true;
						continue;
					}
					Double percentCap = Double.parseDouble(row[2])/Double.parseDouble(row[7]);
					capacities.add(percentCap);
					i++;
				}
				double average = sum(capacities) / i;
				
				//Check the percentages for each station are within bounds
				for(double cap: capacities) {
					if(Math.abs(cap-average) <= .5) {
						checkEqual = true;
					} else {
						checkEqual = false;
					}
					if(checkEqual) {
						break equal_loop;
					}
				}
				i = 0;
				int bikeBuffer = 0;
				//Rebalance the bikes
				for(double cap: capacities) {
					
					if(cap > average) {
						String[] station = stationData.get(i);
						int bikes = Integer.parseInt(station[2]);
						bikes --;
						bikeBuffer++;
						station[2] = Integer.toString(bikes);
					}
					if(cap < average) {
						String[] station = stationData.get(i);
						int bikes = Integer.parseInt(station[2]);
						bikes ++;
						bikeBuffer--;
						station[2] = Integer.toString(bikes);
					}
					i++;
				}
				while(bikeBuffer > 0) {
				for(int x = 0; x <= bikeBuffer; x++) {
					Random rand = new Random();
					int index = rand.nextInt(stationData.size()- 1)+1;
					String[] station = stationData.get(index);
					int bikes = Integer.parseInt(station[2]);
					bikes ++;
					bikeBuffer--;
					station[2] = Integer.toString(bikes);
				} 
				}
				while(bikeBuffer < 0) {
					for(int y = 0; y <= -bikeBuffer; y++) {
						Random rand = new Random();
						int index = rand.nextInt(stationData.size()-1)+1;
						String[] station = stationData.get(index);
						int bikes = Integer.parseInt(station[2]);
						bikes --;
						bikeBuffer ++;
						station[2] = Integer.toString(bikes);
					}
				}
				
				
				
				}
				System.out.println("Stations Equalized!");
		}
		
		}
		scanner.close();
	}
	
	//quits the program
	public static void quit() {
		System.out.println("Goodbye!");
		System.exit(0);
		
	}
	
	//A utility, sums a list
	public static double sum(ArrayList<Double> list) {
		double sum = 0;
		for(double i: list) {
			sum += i;
		}
		return sum;
	}

}
