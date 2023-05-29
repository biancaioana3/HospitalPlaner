package conn;

import java.sql.*;

public class Patients {
    public int id;
    public String adresa;
    public int varsta;
    public Date datanasterii;
    public int gen;
    String nume;
    String prenume;
    String telefon;
    int id_user;
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
                    "gen NUMBER(2),\n" +
                    "id_user NUMBER(5),\n" +
                    "telefon VARCHAR(10),\n" +
                    "created_at DATE DEFAULT SYSDATE,\n" +
                    "updated_at DATE DEFAULT SYSDATE,\n" +
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
    public String selectPatienttDyUserId(int id , String columnName) throws SQLException {
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
    public Patients selectPatientById(int id) throws SQLException {
        Patients patients = new Patients();
        DB conn = new DB();
        Connection connection = conn.getCon();

        Statement statement = connection.createStatement();
        String script =
                "SELECT * FROM PACIENTI WHERE ID=?";
        PreparedStatement stmt = connection.prepareStatement(script);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            patients.id = resultSet.getInt("id");
            patients.nume = resultSet.getString("nume");
            patients.prenume = resultSet.getString("prenume");
            patients.adresa = resultSet.getString("adresa");
            patients.datanasterii = resultSet.getDate("data_nasterii");
            patients.varsta = resultSet.getInt("varsta");
            patients.gen = resultSet.getInt("gen");
            patients.id_user = resultSet.getInt("id_user");
            patients.telefon = resultSet.getString("telefon");
        }

        return patients;
    }
    public void createPatient(){
        createSequens();
        createTable();

        createTrigger();
    }

    public static void main(String[] args) {
        Patients patients = new Patients();
        patients.createPatient();

    }
}
