package conn;
import java.sql.*;
public class DB {
    public String url = "jdbc:oracle:thin:@//localhost:1521/xe" ;
    public Connection con = null;
    public DB(){

        try {
            try {
                con = DriverManager.getConnection(url, "USER_SPITAL", "USER_SPITAL");
                //System.out.println("Connect");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {if (con != null) {
            //System.out.println("Connect");
//                con.close() ;
        }
        }
    }
    public Connection getCon(){
            return con;
    }


}


