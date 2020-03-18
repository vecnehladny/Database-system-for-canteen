package application;

import java.sql.SQLException;
import java.sql.*;

public class SQLTableCreator {

	static String usersString = ""
			+ "CREATE TABLE `users` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) COLLATE utf8_slovak_ci NOT NULL," + 
			"   `ADDRESS` varchar(45) COLLATE utf8_slovak_ci NOT NULL," + 
			"   `PASSWORD` varchar(45) COLLATE utf8_slovak_ci NOT NULL," + 
			"   `EMAIL` varchar(45) COLLATE utf8_slovak_ci NOT NULL," + 
			"   `PRIVILEDGED` tinyint(4) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `EMAIL_UNIQUE` (`EMAIL`)" + 
			" ) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_slovak_ci";
	
	static String foodChefString = ""
			+ "CREATE TABLE `food_chef` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `CHEF_ID` int(11) NOT NULL," + 
			"   `FOOD_ID` int(11) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String chefsString = ""
			+ "CREATE TABLE `chefs` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String foodString = ""
			+ "CREATE TABLE `food` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String foodIngredientsString = ""
			+ "CREATE TABLE `food_ingredients` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `FOOD_ID` int(11) NOT NULL," + 
			"   `INGREDIENTS_ID` int(11) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String ingredientsString = ""
			+ "CREATE TABLE `ingredients` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `NAME` varchar(45) NOT NULL," + 
			"   PRIMARY KEY (`ID`)," + 
			"   UNIQUE KEY `NAME_UNIQUE` (`NAME`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String itemsString = ""
			+ "CREATE TABLE `items` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `ORDER_ID` int(11) NOT NULL," + 
			"   `COUNT` int(11) NOT NULL," + 
			"   PRIMARY KEY (`ID`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	static String ordersString = ""
			+ "CREATE TABLE `orders` (" + 
			"   `ID` int(11) NOT NULL AUTO_INCREMENT," + 
			"   `TIME` timestamp NOT NULL," + 
			"   `USER_ID` int(11) NOT NULL," + 
			"   PRIMARY KEY (`ID`,`TIME`)" + 
			" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci";
	
	public static void createUsers(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(usersString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFoodChef(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(foodChefString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createChefs(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(chefsString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFood(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(foodString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFoodIngredients(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(foodIngredientsString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createIngredients(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(ingredientsString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createItems(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(itemsString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createOrders(Connection connection)
	{
		if(connection == null) {	return;}	
		try {
			PreparedStatement pStatement = connection.prepareStatement(ordersString);
			pStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
