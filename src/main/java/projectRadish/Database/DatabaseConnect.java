package main.java.projectRadish.Database;

import java.sql.*;


public class DatabaseConnect {
	
	public String databaseIP;
	public String databaseName;
	public String databaseUsername;
	public String databasePass;
	DatabaseConnect (String databaseIP, String databaseName, 
			String databaseUsername, String databasePass) {
		
		this.databaseIP = databaseIP;
		this.databaseName = databaseName;
		this.databaseUsername = databaseUsername;
		this.databasePass = databasePass;
		
		
	}
	public void verifyConnection() {
		try {
			//Connection Initialized
			Connection conn = DriverManager.getConnection("jdbc:mysql://162.144.1.48:3306/", this.databaseUsername, this.databasePass);
			//Prepare Statement
			Statement prepareSTMT = conn.createStatement();
			//For Testing, print all usernames. Not to be used during normal run.
			ResultSet returnSet = prepareSTMT.executeQuery("select * FROM twitchinputs");
			while (returnSet.next()) {
				System.out.println(returnSet.getString("username")+", " + returnSet.getString("input_count"));
			}
			 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
