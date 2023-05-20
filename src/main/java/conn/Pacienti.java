package conn;

import java.sql.*;

public class Pacienti {
    public int id;
    public String adresa;
    public int varsta;
    public Date datanasterii;
    public int gen;
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE pacienti_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table pacienti ( \n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "nume VARCHAR(25),\n" +
                    "prenume VARCHAR2(25), \n" +
                    "adresa VARCHAR2(25),\n" +
                    "data_nasterii DATE, \n" +
                    "varsta NUMBER(2),\n" +
                    "gen VARCHAR(10),\n" +
                    "id_user NUMBER(5),\n" +
                    "telefon VARCHAR(10),\n" +
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
                    "CREATE OR REPLACE TRIGGER pacienti_trigger\n" +
                            "BEFORE INSERT ON PACIENTI\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT pacienti_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    public String selectPacientDyUserId(int id , String columnName) throws SQLException {
        DB conn = new DB();
        Connection connection = conn.getCon();

        Statement statement = connection.createStatement();
        String script =
                "SELECT " +columnName+" FROM PACIENTI WHERE ID_USER=?";
        PreparedStatement stmt = connection.prepareStatement(script);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            return String.valueOf(resultSet.getString(columnName));
        }

        return "SELECT ERROR!";
    }
    public void createPacienti(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        Pacienti pacienti = new Pacienti();
        pacienti.createPacienti();

    }
}
