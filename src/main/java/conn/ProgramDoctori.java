package conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProgramDoctori {
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE program_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table program(\n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "id_medic NUMBER(5),\n" +
                    "luni NUMBER(1),\n" +
                    "marti NUMBER(1),\n" +
                    "miercuri NUMBER(1),\n" +
                    "joi NUMBER(1),\n" +
                    "vineri NUMBER(1),\n" +
                    "FOREIGN KEY (id_medic) REFERENCES medici(id)\n" +
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
                    "CREATE OR REPLACE TRIGGER program_trigger\n" +
                            "BEFORE INSERT ON program\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT program_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public ProgramDoctori(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        ProgramDoctori programDoctori = new ProgramDoctori();

    }
}
