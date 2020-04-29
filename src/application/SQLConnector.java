package application;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import ui.Filter;
import ui.Paging;

import data.User;
import ui.admin.ChefsVBoxController;
import ui.admin.FoodVBoxController;
import data.Chef;
import data.FoodItem;
import data.Ingredient;

//Tato classa sluzi na pripojenie k MySQL a vykonanie Queries
public class SQLConnector {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
   
    private String username = "root";
    private String password = "root";
    
    private static EntityManagerFactory ENTITY_MANAGER = Persistence.createEntityManagerFactory("MyEntManager");
       
    
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
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	boolean result = true;
    	EntityTransaction entityTransaction = null;
    	
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		User user = new User(name,address,email,false,MD5Hashing.getSecurePassword(password));
    		entityManager.persist(user);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		result = false;
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }

    //Skontroluje, ci sa v DB nachadza dany pouzivatel aj s heslom ak ano vrati objekt User    
    public User getUserInDB(String email, String password)
    {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	User user = null;
    	
    	try {
    		String query = "SELECT u FROM User u WHERE u.email = :email"; 
    		TypedQuery<User> typedQuery = entityManager.createQuery(query,User.class);
    		typedQuery.setParameter("email",email);
    		user = typedQuery.getSingleResult();
    		
    		//Al ke vysledok zly tak vratime null
    		if(!user.getPassword().equals(MD5Hashing.getSecurePassword(password))) {
    			user = null;
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return user;
    }
    
    public User getUserInDBByEmail(String email)
    {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	User user = null;
    	
    	try {
    		String query = "SELECT u FROM User u WHERE u.email = :email"; 
    		TypedQuery<User> typedQuery = entityManager.createQuery(query,User.class);
    		typedQuery.setParameter("email",email);
    		user = typedQuery.getSingleResult();
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return user;
    }
    
    public List<User> getSearchUsersInDB(String email, Paging f)
    {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<User> users = null;
    	
    	try {   		
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT u FROM User u WHERE u.email LIKE :email ORDER BY u.id ASC"; 
    		TypedQuery<User> typedQuery = entityManager.createQuery(query,User.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());;
    		typedQuery.setParameter("email","%"+email+"%");
    		users = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return users;
    }
    
    public List<User> getUserListFromDB(Paging f) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<User> users = null;
    	
    	try {
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT u FROM User u ORDER BY u.id ASC"; 
    		TypedQuery<User> typedQuery = entityManager.createQuery(query,User.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());;
    		users = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return users;
    }
    
    public boolean updateUserDB(User u, boolean changePass) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	User user = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		user = entityManager.find(User.class, u.getId());
    		user.setAddress(u.getAddress());
    		user.setName(u.getName());
    		user.setPriviledged(u.isPriviledged());
    		if(changePass) {
    			user.setPassword(u.getPassword());
    		}
    		
    		entityManager.persist(user);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    public boolean removeUserFromDB(User u) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	User user = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		user = entityManager.find(User.class, u.getId());
    		entityManager.remove(user);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    public List<Chef> getSearchChefsInDB(String name, Paging f)
    {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<Chef> chefs = null;
    	
    	try {   		
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT ch FROM Chef ch WHERE ch.name LIKE :name ORDER BY ch.id ASC"; 
    		TypedQuery<Chef> typedQuery = entityManager.createQuery(query,Chef.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());;
    		typedQuery.setParameter("name","%"+name+"%");
    		chefs = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return chefs;
    }
    
    public List<Chef> getChefListFromDB(Paging f) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<Chef> chefs = null;
    	
    	try {
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT ch FROM Chef ch ORDER BY ch.id ASC"; 
    		TypedQuery<Chef> typedQuery = entityManager.createQuery(query,Chef.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());;
    		chefs = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return chefs;
    }
    
    public boolean updateChefDB(Chef ch) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	Chef chef = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		chef = entityManager.find(Chef.class, ch.getId());
    		chef.setName(ch.getName());
    		
    		entityManager.persist(chef);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    public boolean removeChefFromDB(Chef ch) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	Chef chef = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		chef = entityManager.find(Chef.class, ch.getId());
    		entityManager.remove(chef);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    public List<Ingredient> getSearchIngsInDB(String name, Paging f)
    {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<Ingredient> ingredients = null;    	
    	try {  		   		
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT i FROM Ingredient i WHERE i.name LIKE :name ORDER BY i.id ASC"; 
    		TypedQuery<Ingredient> typedQuery = entityManager.createQuery(query,Ingredient.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());
    		typedQuery.setParameter("name","%"+name+"%");
    		ingredients = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return ingredients;
    }
    
    public List<Ingredient> getIngredientListFromDB(Paging f){
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	List<Ingredient> ingredients = null;    	
    	try {  		   		
            int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
    		String query = "SELECT i FROM Ingredient i ORDER BY i.id ASC"; 
    		TypedQuery<Ingredient> typedQuery = entityManager.createQuery(query,Ingredient.class)
    				.setFirstResult(startFrom).setMaxResults((int)f.getResultsPerPage());
    		ingredients = typedQuery.getResultList();
    		    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		entityManager.close();
    	}
    	
    	return ingredients;
    }
    
    public boolean updateIngDB(Ingredient ing) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	Ingredient ingredient = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		ingredient = entityManager.find(Ingredient.class, ing.getId());
    		ingredient.setName(ing.getName());
    		
    		entityManager.persist(ingredient);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    public boolean removeIngFromDB(Ingredient ing) {
    	EntityManager entityManager = ENTITY_MANAGER.createEntityManager();
    	EntityTransaction entityTransaction = null;
    	Ingredient ingredient = null;
    	boolean result = true;
    	try {
    		entityTransaction = entityManager.getTransaction();
    		entityTransaction.begin();
    		
    		ingredient = entityManager.find(Ingredient.class, ing.getId());
    		entityManager.remove(ingredient);
    		entityTransaction.commit();
    		
    	}catch(Exception e) {
    		if(entityTransaction !=null) {
    			entityTransaction.rollback();
    		}
    		result = false;
    		e.printStackTrace();
    	}finally {
    		entityManager.close();
    	}
    	
    	return result;
    }
    
    //Stop ORM functions
    
    public ArrayList<FoodItem> getFoodListFromDB(Paging f){
        
        if(connection == null) {    return null;}

        f.setFiltered(false);
        int startFrom = (f.getPage() - 1) * (int)(f.getResultsPerPage());
        ArrayList<FoodItem> list = new ArrayList<FoodItem>();

        try {
            connection.setAutoCommit(false);
            //zistim kolko je zaznamov v tabulke 
            //f.setNumberOfRecords(getRecordNumberFromDB("food"));

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
                float price = resultSet.getFloat(3);
                String chef = resultSet.getString(4);
                String ingredientsString = resultSet.getString(5);
                ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                
                if(ingredientsString == null){
                    ingredients.clear();
                }
                else {
                    if(ingredientsString.contains(",")){
                        String[] splitted = ingredientsString.split(",");
                        for(String s : splitted){
                            ingredients.add(new Ingredient(s));
                        }
                    }
                    else {
                        ingredients.add(new Ingredient(ingredientsString));
                    }   
                }
                list.add(new FoodItem(id,name,price,chef,ingredients));
            }
            connection.commit();
            return list;

        } catch (SQLException e) {
            System.out.println("Problem with loading food from database");
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return null;
        }

    }

    public ArrayList<FoodItem> getFoodListFromDB(Paging p, Filter f) {
		if(connection == null) {    return null;}

        p.setFiltered(true);
        int startFrom = (p.getPage() - 1) * (int)(p.getResultsPerPage());
        ArrayList<FoodItem> list = new ArrayList<FoodItem>();

        StringBuilder cmd = new StringBuilder("SELECT f.ID, f.NAME, f.PRICE, ch.NAME, GROUP_CONCAT(i.name) as igroup FROM food f JOIN food_chef fc ON fc.FOOD_ID = f.ID JOIN chefs ch ON ch.ID = fc.CHEF_ID LEFT JOIN food_ingredients fi ON fi.FOOD_ID = f.ID LEFT JOIN ingredients i ON fi.INGREDIENTS_ID = i.ID ");
        cmd.append("GROUP BY fc.ID ");
        cmd.append("HAVING f.PRICE <= " + f.getMaxPrice() + " ");

        if(!f.getShouldContain().isEmpty()){
            for(String s : f.getShouldContain()){
                cmd.append("AND igroup LIKE '%" + s + "%' ");
            }
        }

        if(!f.getShouldNotContain().isEmpty()){
            for(String s : f.getShouldNotContain()){
                cmd.append("AND igroup NOT LIKE '%" + s + "%' ");
            }
        }

        if(!f.getChefName().isEmpty()){
            cmd.append("AND ch.NAME LIKE '%" + f.getChefName() + "%' ");
        }

        if(!f.getFoodName().isEmpty()){
            cmd.append("AND f.NAME LIKE '%" + f.getFoodName() + "%' ");
        }
        

        cmd.append("ORDER BY fc.ID ASC LIMIT ");
        
        System.out.println(cmd);

        try {
            connection.setAutoCommit(false);
            //zistim kolko je zaznamov v tabulke 
            //p.setNumberOfRecords(getRecordNumberFromDB("food"));

            //Pocet stran na ktore rozdelim zaznamy
            p.setTotalPages(Math.ceil(p.getNumberOfRecords() / p.getResultsPerPage()));

            //vyberiem z tabulky potrebny pocet zaznamov na stranu
            preparedStatement = connection.prepareStatement(cmd.toString() + "?,?;");
            preparedStatement.setInt(1, startFrom);
            preparedStatement.setInt(2, (int)(p.getResultsPerPage()));
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                float price = resultSet.getFloat(3);
                String chef = resultSet.getString(4);
                String ingredientsString = resultSet.getString(5);
                ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
                
                if(ingredientsString == null){
                    ingredients.clear();
                }
                else {
                    if(ingredientsString.contains(",")){
                        String[] splitted = ingredientsString.split(",");
                        for(String s : splitted){
                            ingredients.add(new Ingredient(s));
                        }
                    }
                    else {
                        ingredients.add(new Ingredient(ingredientsString));
                    }   
                }
                list.add(new FoodItem(id,name,price,chef,ingredients));
            }

            resultSet.close();
            connection.commit();
            return list;

        } catch (SQLException e) {
            System.out.println("Problem with loading food from database");
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return null;
        }

    }


    public int getRecordNumberFromDB(String table){
        if(connection == null) {return 0;}
        int count = 0;

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + table);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                count = resultSet.getInt(1);
                System.out.println(count);
            }
            connection.commit();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return count;
        }
    }

    public float getMaxPriceFromDB(){
        
        if(connection == null) {return 0;}
        float maxPrice = 0;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT MAX(food.PRICE) FROM food");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                maxPrice = resultSet.getFloat(1);
            }
            connection.commit();
            return maxPrice;
            
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return maxPrice;
        }
    }

    public float getMinPriceFromDB(){
        
        if(connection == null) {return 0;}
        float minPrice = 0;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT MIN(food.PRICE) FROM food");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                minPrice = resultSet.getFloat(1);
            }
            connection.commit();
            return minPrice;
            
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return minPrice;
        }
    }

    public ArrayList<Ingredient> getIngredientsListFromDB(){
        
        if(connection == null) {    return null;}

        ArrayList<Ingredient> list = new ArrayList<Ingredient>();

        try {
            connection.setAutoCommit(false);
            //vyberiem z tabulky potrebny pocet zaznamov na stranu
            preparedStatement = connection.prepareStatement("SELECT DISTINCT res.ingNAME FROM (SELECT i.ID AS ingID, REGEXP_SUBSTR(i.NAME,'[A-z[[:space:]]]+') AS ingNAME FROM ingredients AS i) as res");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString(1);
                
                list.add(new Ingredient(name));
            }
            connection.commit();
            return list;

        } catch (SQLException e) {
            System.out.println("Problem with loading Ingredients from database");
            e.printStackTrace();
            try{
                if(connection != null)
                    connection.rollback();
            }catch(SQLException r){
                System.out.println(r.getMessage());
            }
            return null;
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
