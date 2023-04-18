import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    //this function creates and returns a database connection 
    protected static Connection initializeDatabase() throws SQLException, ClassNotFoundException { 
		// Initialize all the information regarding 
		// Database Connection 
                //Database driver
		String dbDriver = "com.mysql.jdbc.Driver"; 
                //Database URL
		String dbURL = "jdbc:mysql://localhost:3306/";
		// Database name to access 
		String dbName = "efthimeros_db"; 
		String dbUsername = "efthimeros"; 
		String dbPassword = "4321"; 

		Class.forName(dbDriver); 
		Connection con = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword); 
		return con; 
	} 
    
}
