
import java.sql.DriverManager;
import java.sql.*;

public class ConnectDatabase {
    static Connection con;

    static {
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/idbcbank","root","Solanki@0000");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
