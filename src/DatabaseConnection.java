import java.io.*;
import java.sql.*;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:security_issues.db";

    public Connection getConnection() {
        Connection connection = null;
        try {
        connection = DriverManager.getConnection(DB_URL);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
}