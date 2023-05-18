package conn;

import java.sql.*;

public class Medici {
    public int id;

    public int specializare;
    public int luni;
    public int marti;
    public int miercuri;
    public int joi;
    public int vineri;
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE medici_seq START WITH 1 INCREMENT BY 1";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    public void createTable(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
        String script="create table medici ( \n" +
                "idmedic NUMBER(5) PRIMARY KEY, \n" +
                "numemedic VARCHAR2(25), \n" +
                "prenumemedic VARCHAR2(25), \n" +
                "datanasterii DATE, \n" +
                "idsectie NUMBER(5),\n" +
                "iduser NUMBER(5),\n" +
                "nrtelefon VARCHAR(10)\n" +
                ")";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    public void createTrigger(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script =
                    "CREATE OR REPLACE TRIGGER medici_trigger\n" +
                    "BEFORE INSERT ON MEDICI\n" +
                    "FOR EACH ROW\n" +
                    "BEGIN\n" +
                    "  SELECT medici_seq.NEXTVAL INTO :NEW.IDMEDIC FROM DUAL;\n" +
                    "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public String selectMedicDyUserId(int id , String columnName) throws SQLException {
        DB conn = new DB();
        Connection connection = conn.getCon();

             Statement statement = connection.createStatement();
            String script =
                    "SELECT " +columnName+" FROM MEDICI WHERE IDUSER=?";
            PreparedStatement stmt = connection.prepareStatement(script);
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                return String.valueOf(resultSet.getString(columnName));
            }

        return "SELECT ERROR!";
    }

    public void createMedici(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        Medici medici = new Medici();


    }
}
