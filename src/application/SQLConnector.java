package application;

import java.sql.*;

//Tato classa sluzi na pripojenie k MySQL a vykonanie Queries
public class SQLConnector {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    //Nacitanie drivera a pripojenie k databaze
    public void connectToDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            return;
        }

        try {
            //Amazon databaza - mozeme ju kludne pouzit namiesto localhostu 
            connection = DriverManager
                    .getConnection("jdbc:mysql://vava-db.ctknqglftm5b.eu-central-1.rds.amazonaws.com/dbsDB?characterEncoding=latin1",
                            "masteradmin", "vavadatabaza123.");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

        //Debug If, neskor zmazat
        if (connection != null) {
            System.out.println("Connection established");
        } else {
            System.out.println("Failed to make connection!");
        }
    }

    //Skontroluje, ci sme pripojeny k databaze
    public boolean isConnectedToDB()
    {
        return connection != null;
    }

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
            System.out.println("Problem with adding user, check logs");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Skontroluje, ci sa v DB nachadza dany pouzivatel aj s heslom a vrati jeho ID
    public int getUserInDB(String email, String password)
    {
        if(connection == null) {    return -1;}
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

                if(recievedPass.equals(MD5Hashing.getSecurePassword(password))
                    && email.equals(recievedEmail)) {
                    return resultSet.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Problem with checking user, check logs");
            e.printStackTrace();
            return -1;
        }

        return -1;
    }

    //Zmaze cely obsah tabulky - iba na test
    public void deleteUserDB()
    {
        try {
            statement = connection.createStatement();

            statement.execute("DELETE from users");
            //Reset autoincrement
            statement.execute("ALTER TABLE dbsDB.users AUTO_INCREMENT = 1;");

            System.out.println("USERS TABLE DELETED");

        } catch (SQLException e) {
            System.out.println("Problem with deleting users table, check logs");
            e.printStackTrace();
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
        } catch (Exception e) {
            System.out.println("Error closing connection");
            e.printStackTrace();
        }
        System.out.println("Connections closed");
    }
}
