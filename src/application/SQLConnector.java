package application;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;

import ui.Controller;
import ui.Paging;

import data.User;
import ui.admin.ChefsVBoxController;
import ui.admin.FoodVBoxController;
import data.Chef;
import data.FoodItem;
import data.Ingredients;

//Tato classa sluzi na pripojenie k MySQL a vykonanie Queries
public class SQLConnector {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
   
    private String username = "root";
    private String password = "infernoinferno";
    
    
    //Nacitanie drivera a pripojenie k databaze
    public void connectToDB()
    {
    	//Inicializacia ovladaca
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found");
            e.printStackTrace();
            return;
        }

        try {
        	//Localhost databaza - bude lepsia pri 1milione zaznamoch
        	connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/dbs_db?characterEncoding=latin1",username, password);
            System.out.println("Database Connected");
        	
             
        } catch (SQLException e) {              	
            System.out.println("Error code "+e.getErrorCode());
            
            //V pripade, ze databaza neexistuje tak ju vytvorime
            if(e.getErrorCode() == 1049) {
            	Connection baseConnection = getMysqlConnection();
            	SQLDatabaseCreator.createDatabase(baseConnection,this);
            }
            
            return;
        }
    }
    
    //Pripojenie celkovo na localhost nie na konkretnu databazu
    private Connection getMysqlConnection()
    {
    	try {
        	connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/?characterEncoding=latin1",username, password);        	
        	return connection;
        } catch (SQLException e) {              	
        	e.printStackTrace();
            return null;
        }
    }
    
    public Connection getConnection() {	return connection;	}

    //Skontroluje, ci sme pripojeny k databaze
    public boolean isConnectedToDB() {	return connection != null;	}

    //Prida pouzivate do databazy.
    //Vrati true ak bola akcia uspesna inak false
    public boolean addUserToDB(String name,String address, String password,String email)
    {
        if(connection == null) {return false;}

        try {
            preparedStatement = connection
                    .prepareStatement("INSERT INTO users (NAME,ADDRESS,PASSWORD,EMAIL,PRIVILEDGED) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, MD5Hashing.getSecurePassword(password));
            preparedStatement.setString(4, email);
            preparedStatement.setBoolean(5, false);
            preparedStatement.executeUpdate();

            System.out.println("User: "+email+" passw: "+password+" added.");
        } catch (SQLException e) {
            System.out.println("Problem with adding user - code "+ e.getErrorCode());
            
            if(e.getErrorCode() == 1062){
            	System.out.println("User already exists!");
            }
            else {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }


    //Skontroluje, ci sa v DB nachadza dany pouzivatel aj s heslom ak ano vrati objekt User
    public User getUserInDB(String email, String password)
    {
        if(connection == null) {    return null;}
        try {
            preparedStatement = connection
                    .prepareStatement("SELECT * FROM users WHERE EMAIL = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            System.out.println("Checking if user exists");

            //Porovnava aj email kedze select je case insensitive
            while(resultSet.next())
            {
                String recievedPass = resultSet.getString("PASSWORD");
                String recievedEmail = resultSet.getString("EMAIL");

                if(recievedPass.equals(MD5Hashing.getSecurePassword(password)) && email.equals(recievedEmail)) {
                        User temp = new User(resultSet.getInt("ID"),resultSet.getString("NAME"),resultSet.getString("ADDRESS"),resultSet.getString("EMAIL"),resultSet.getBoolean("PRIVILEDGED"));
                        return temp;
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem with checking user");
            e.printStackTrace();
            return null;
        }

        return null;
    }

    //Zmaze cely obsah tabulky - iba na test
    public void deleteUserDB()
    {
        try {
            statement = connection.createStatement();

            statement.execute("DELETE from users");
            //Reset autoincrement
            statement.execute("ALTER TABLE dbs_db.users AUTO_INCREMENT = 1;");

            System.out.println("USERS TABLE DELETED");

        } catch (SQLException e) {
            System.out.println("Problem with deleting users table");
            e.printStackTrace();
        }
    }

    public ArrayList<FoodItem> getFoodListFromDB(Paging f){
        
        if(connection == null) {    return null;}

        int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
        ArrayList<FoodItem> foodList = new ArrayList<FoodItem>();

        try {
            //zistim kolko je zaznamov v tabulke 
            f.setNumberOfRecords(getRecordNumberFromDB("food"));

            //Pocet stran na ktore rozdelim zaznamy
            f.setTotalPages(Math.ceil(f.getNumberOfRecords() / f.getResultsPerPage()));

            //vyberiem z tabulky potrebny pocet zaznamov na stranu
            preparedStatement = connection.prepareStatement("SELECT f.ID, f.NAME, f.PRICE, ch.NAME, GROUP_CONCAT(i.name) FROM food f JOIN food_chef fc ON fc.FOOD_ID = f.ID JOIN chefs ch ON ch.ID = fc.CHEF_ID LEFT JOIN food_ingredients fi ON fi.FOOD_ID = f.ID LEFT JOIN ingredients i ON fi.INGREDIENTS_ID = i.ID GROUP BY fc.ID ORDER BY fc.ID ASC LIMIT ?, ?");
            preparedStatement.setInt(1, startFrom);
            preparedStatement.setInt(2, (int)(f.getResultsPerPage()));
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int price = resultSet.getInt(3);
                String chef = resultSet.getString(4);
                String ingredientsString = resultSet.getString(5);
                ArrayList<Ingredients> ingredient = new ArrayList<Ingredients>();
                
                if(ingredientsString == null){
                    ingredient.clear();
                }
                else {
                    if(ingredientsString.contains(",")){
                        String[] splitted = ingredientsString.split(",");
                        for(String s : splitted){
                            ingredient.add(new Ingredients(s));
                        }
                    }
                    else {
                        ingredient.add(new Ingredients(ingredientsString));
                    }   
                }
                foodList.add(new FoodItem(id,name,price,chef,ingredient));
            }

            return foodList;

        } catch (SQLException e) {
            System.out.println("Problem with loading food from database");
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<Chef> getChefListFromDB(ChefsVBoxController f){
        
        if(connection == null) {    return null;}

        int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
        ArrayList<Chef> chefList = new ArrayList<Chef>();

        try {
            //zistim kolko je zaznamov v tabulke
            f.setNumberOfRecords(getRecordNumberFromDB("chefs"));
            
            //Pocet stran na ktore rozdelim zaznamy
            f.setTotalPages(Math.ceil(f.getNumberOfRecords() / f.getResultsPerPage()));

            //vyberiem z tabulky potrebny pocet zaznamov na konkretnu stranu
            preparedStatement = connection.prepareStatement("SELECT * FROM chefs ORDER BY ID ASC LIMIT ? , ?");
            preparedStatement.setInt(1, startFrom);
            preparedStatement.setInt(2, (int)(f.getResultsPerPage()));
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                
                chefList.add(new Chef(id,name));
            }

            return chefList;

        } catch (SQLException e) {
            System.out.println("Problem with loading chefs from database");
            e.printStackTrace();
            return null;
        }

    }

    public int getRecordNumberFromDB(String table){
        if(connection == null) {return 0;}
        int count = 0;

        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + table);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                count = resultSet.getInt(1);
                System.out.println(count);
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return count;
        }
    }

    //Zavrie otvorene pripojenia
    public void closeConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }

            System.out.println("Connection Closed");
            
        } catch (Exception e) {
            System.out.println("Error closing connection");
            e.printStackTrace();
        }
    }
}
