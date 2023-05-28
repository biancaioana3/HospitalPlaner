package conn;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Documents extends JDialog {

    public int id;
    public String path;
    public int id_medic;
    public int id_pacient;
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE document_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table documente(\n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "id_medic NUMBER(5),\n" +
                    "id_pacient NUMBER(5),\n" +
                    "document_type NUMBER(5),\n" +
                    "path VARCHAR(60),\n" +
                    "created_at DATE DEFAULT SYSDATE,\n" +
                    "updated_at DATE DEFAULT SYSDATE,\n" +
                    "FOREIGN KEY (id_medic) REFERENCES medici(id),\n" +
                    "FOREIGN KEY (id_pacient) REFERENCES pacienti(id)\n" +
                    ")\n";
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
                    "CREATE OR REPLACE TRIGGER document_trigger\n" +
                            "BEFORE INSERT ON documente\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT document_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public Documents(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        Documents document = new Documents();

    }
}
