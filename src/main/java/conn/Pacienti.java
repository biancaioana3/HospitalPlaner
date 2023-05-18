package conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Pacienti {
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
                    "idpacient NUMBER(5) PRIMARY KEY,\n" +
                    "numepacient VARCHAR(25),\n" +
                    "prenumepacient VARCHAR2(25), \n" +
                    "adresa VARCHAR2(25),\n" +
                    "datanasterii DATE, \n" +
                    "varsta NUMBER(2),\n" +
                    "gen VARCHAR(10),\n" +
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
                    "CREATE OR REPLACE TRIGGER pacienti_trigger\n" +
                            "BEFORE INSERT ON PACIENTI\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT pacienti_seq.NEXTVAL INTO :NEW.IDPACIENT FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public Pacienti(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        Pacienti pacienti = new Pacienti();

    }
}
