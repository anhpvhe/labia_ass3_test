package lab.ia.ExpenseManagement.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}