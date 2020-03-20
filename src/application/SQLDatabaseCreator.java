package application;

import java.sql.SQLException;
import java.sql.*;

public class SQLDatabaseCreator {

	static String databaseString = "CREATE DATABASE `dbs_db`;";
	
	static String usersString = ""
			+ "CREATE TABLE `users` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   `ADDRESS` varchar(45) NOT NULL," + 
			"   `PASSWORD` varchar(45) NOT NULL," + 
			"   `EMAIL` varchar(45) NOT NULL," + 
			"   `PRIVILEDGED` bool NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8;";
	
	static String foodChefString = ""
			+ "CREATE TABLE `food_chef` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `CHEF_ID` int NOT NULL," + 
			"   `FOOD_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String chefsString = ""
			+ "CREATE TABLE `chefs` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String foodString = ""
			+ "CREATE TABLE `food` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String foodIngredientsString = ""
			+ "CREATE TABLE `food_ingredients` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `FOOD_ID` int NOT NULL," + 
			"   `INGREDIENTS_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String ingredientsString = ""
			+ "CREATE TABLE `ingredients` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String itemsString = ""
			+ "CREATE TABLE `items` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `ORDER_ID` int NOT NULL," + 
			"   `COUNT` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	static String ordersString = ""
			+ "CREATE TABLE `orders` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `TIME` timestamp NOT NULL," + 
			"   `USER_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`,`TIME`)" + 
			" ) "
			+ "ENGINE=InnoDB "
			+ "DEFAULT CHARSET=utf8mb4;";
	
	public static void createDatabase(Connection connection,SQLConnector connector)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(databaseString);
			pStatement.execute();		
			
			connector.connectToDB();
			Connection newConnection = connector.getConnection();
			createTables(newConnection);
			
		} catch (SQLException e) {
	        if (e.getErrorCode() == 1007) {
	            // Database already exists error
	            System.out.println(e.getMessage());
	        } else {
	            e.printStackTrace();
	        }
		}
	}
	
	public static void createTables(Connection connection)
	{
		createTable(connection,usersString);
		createTable(connection,chefsString);
		createTable(connection,foodString);
		createTable(connection,ingredientsString);
		createTable(connection,foodChefString);
		createTable(connection,foodIngredientsString);
		createTable(connection,ordersString);
		createTable(connection,itemsString);
	}
	
	private static void createTable(Connection connection, String query)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(query);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	
}
