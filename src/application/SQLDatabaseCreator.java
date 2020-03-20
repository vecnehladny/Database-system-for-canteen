package application;

import java.sql.SQLException;
import java.sql.*;

public class SQLDatabaseCreator {

	static String databaseString = "CREATE DATABASE `dbs_db`;";
	
	static String usersString = "" +
			"	CREATE TABLE `users` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   `ADDRESS` varchar(45) NOT NULL," + 
			"   `PASSWORD` varchar(45) NOT NULL," + 
			"   `EMAIL` varchar(45) NOT NULL," + 
			"   `PRIVILEDGED` bool NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String foodChefString = "" +
			"	CREATE TABLE `food_chef` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `CHEF_ID` int NOT NULL," + 
			"   `FOOD_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`), "	+
			"	CONSTRAINT `fc_chefs` FOREIGN KEY (`CHEF_ID`) REFERENCES `chefs` (ID)," + 
			"	CONSTRAINT `fc_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (ID)" + 
			"	) "	+
			"	ENGINE=InnoDB "	+
			"	DEFAULT CHARSET=utf8;";
	
	static String chefsString = "" +
			"	CREATE TABLE `chefs` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String foodString = "" +
			"	CREATE TABLE `food` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   `PRICE` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String foodIngredientsString = "" +
			"	CREATE TABLE `food_ingredients` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `FOOD_ID` int NOT NULL," + 
			"   `INGREDIENTS_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"	CONSTRAINT `fi_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (ID)," + 
			"	CONSTRAINT `fi_ingredients` FOREIGN KEY (`INGREDIENTS_ID`) REFERENCES `ingredients` (ID)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String ingredientsString = ""+
			"	CREATE TABLE `ingredients` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String itemsString = "" +
			"	CREATE TABLE `items` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `FOOD_ID` int NOT NULL," + 
			"   `COUNT` int NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"	CONSTRAINT `i_food` FOREIGN KEY (`FOOD_ID`) REFERENCES `food` (ID)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String ordersString = "" +
			"	CREATE TABLE `orders` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `TIME` timestamp NOT NULL," + 
			"   `USER_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`,`TIME`)," + 
			"	CONSTRAINT `o_users` FOREIGN KEY (`USER_ID`) REFERENCES `users` (ID)" + 
			"	) " +
			"	ENGINE=InnoDB " +
			"	DEFAULT CHARSET=utf8;";
	
	static String itemsOrdersString = "" +
			"	CREATE TABLE `items_orders` (" + 
			"   `ID` int NOT NULL AUTO_INCREMENT," + 
			"   `ITEM_ID` int NOT NULL," + 
			"   `ORDER_ID` int NOT NULL," + 
			"   PRIMARY KEY (`ID`), "	+
			"	CONSTRAINT `io_item` FOREIGN KEY (`ITEM_ID`) REFERENCES `items` (ID)," + 
			"	CONSTRAINT `io_order` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (ID)" + 
			"	) "	+
			"	ENGINE=InnoDB "	+
			"	DEFAULT CHARSET=utf8;";
	
	public static void createDatabase(Connection connection,SQLConnector connector)
	{
		if(connection == null) {	return;}	
		try {
			//Vytvorenie novej databazy
			PreparedStatement pStatement = connection.prepareStatement(databaseString);
			pStatement.execute();		
			
			//Ziskanie pripojenia k novej databaze
			connector.connectToDB();
			Connection newConnection = connector.getConnection();
			createTables(newConnection);
			
		} catch (SQLException e) {
	            e.printStackTrace();
		}
	}
	
	//Vytvorenie jednotlivych tabuliek
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
		createTable(connection,itemsOrdersString);
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
