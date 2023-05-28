package conn;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DocumentType extends JDialog {
    public int id;
    public String nume;
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE document_type_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table document_type(\n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "nume VARCHAR(60),\n" +
                    "created_at DATE DEFAULT SYSDATE,\n" +
                    "updated_at DATE DEFAULT SYSDATE\n" +
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
                    "CREATE OR REPLACE TRIGGER document_type_trigger\n" +
                            "BEFORE INSERT ON document_type\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT document_type_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public DocumentType(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        DocumentType document = new DocumentType();

    }
}
