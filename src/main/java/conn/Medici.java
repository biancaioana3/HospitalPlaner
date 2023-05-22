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
    public int id_user;
    public String nume;
    public String prenume;
    public String telefon;

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
                "id NUMBER(5) PRIMARY KEY, \n" +
                "nume VARCHAR2(25), \n" +
                "prenume VARCHAR2(25), \n" +
                "data_nasterii DATE, \n" +
                "id_sectie NUMBER(5),\n" +
                "id_user NUMBER(5),\n" +
                "telefon VARCHAR(10),\n" +
                "created_at DATE DEFAULT SYSDATE,\n" +
                "updated_at DATE DEFAULT SYSDATE,\n" +
                "FOREIGN KEY (id_sectie) REFERENCES SECTII(id),\n" +
                "FOREIGN KEY (id_user) REFERENCES USERS(id)\n" +
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
                    "  SELECT medici_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
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
                    "SELECT " +columnName+" FROM MEDICI WHERE ID_USER=?";
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
        medici.createMedici();


    }
}
