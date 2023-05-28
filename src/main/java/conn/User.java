package conn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    public String nume;
    public int id;
    public String prenume;
    public String email;
    public String telefon;
    public String parola;
    public int isMedic;

    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table users(\n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "is_medic NUMBER(1,0),\n" +
                    "email VARCHAR2(60) UNIQUE,\n" +
                    "parola VARCHAR2(60),\n" +
                    "nume VARCHAR2(60),\n" +
                    "prenume VARCHAR2(60),\n" +
                    "telefon VARCHAR2(10),\n" +
                    "created_at DATE DEFAULT SYSDATE,\n" +
                    "updated_at DATE DEFAULT SYSDATE\n"+
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
                    "CREATE OR REPLACE TRIGGER users_trigger\n" +
                            "BEFORE INSERT ON USERS\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT users_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }


    public void insertMedicTrigger(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script =
                    "CREATE OR REPLACE TRIGGER inserare_medic_trigger\n" +
                            "AFTER INSERT ON USERS\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  IF (:NEW.is_medic = 1) THEN\n" +
                            "  INSERT INTO doctors (id_user, nume, prenume, telefon)\n" +
                            "  VALUES (:NEW.id, :NEW.nume, :NEW.prenume, :NEW.telefon);\n" +
                            "  END IF;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }

    public void insertPacientTrigger(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script =
                    "CREATE OR REPLACE TRIGGER inserare_pacient_trigger\n" +
                            "AFTER INSERT ON USERS\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  IF (:NEW.is_medic = 0) THEN\n" +
                            "  INSERT INTO patients (id_user, nume, prenume, telefon)\n" +
                            "  VALUES (:NEW.id, :NEW.nume, :NEW.prenume, :NEW.telefon);\n" +
                            "  END IF;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    public void createUser(){
        createSequens();
        createTable();
        createTrigger();
        //insertMedicTrigger();
    }
    public static void main(String[] args) {
        User user = new User();
//        user.createUser();
        user.insertMedicTrigger();
        user.insertPacientTrigger();
    }


}
