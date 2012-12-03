package DB;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DB {

    public Connection conn;

    public void close(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public DB() {
        DataSource ds;
        String serverName = "127.0.0.1";
        String myDatabase = "comet";
        String username = "root";
        String password = "";
        String url = "jdbc:mysql://" + serverName + ":3306/"
                + myDatabase + "?user=" + username + "&" + "password=" + password;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url);//("jdbc:odbc:AAA");                        
        } catch (Exception ex) {

            System.out.println("Load Driver Exception: " + ex.toString());
        }
    }

    public ResultSet getResultSet(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("SQL:"+sql);
        }
        return rs;
    }

    public void executeUpdate(String sql) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("SQL:"+sql);
        }
    }

    public long executeInsert(String sql) {
        Statement stmt = null;
        long autoinckey = -1;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if (rs.next()) {
                autoinckey = rs.getLong(1);
            }
            rs.close();
            rs = null;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("SQL:"+sql);
        }
        return autoinckey;
    }
    public static void main(String[] args) throws Exception {
        new DB().getResultSet("select * from area");
    }
}
