package conn;

import java.sql.*;
import java.util.Date;

public class Programari {
    public int id;
    public int id_medic;
    public int id_pacient;
    public Date date;
    public String ora;
    public void createSequens(){
        DB conn = new DB();
        Connection connection = conn.getCon();
        try (connection;
             Statement statement = connection.createStatement()) {
            String script = "CREATE SEQUENCE programari_seq START WITH 1 INCREMENT BY 1";
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
            String script="create table programari(\n" +
                    "id NUMBER(5) PRIMARY KEY,\n" +
                    "id_medic NUMBER(5),\n" +
                    "id_pacient NUMBER(5),\n" +
                    "data DATE,\n" +
                    "ora VARCHAR(8),\n" +
                    "created_at DATE DEFAULT SYSDATE,\n" +
                    "updated_at DATE DEFAULT SYSDATE,\n" +
                    "FOREIGN KEY (id_medic) REFERENCES medici(id),\n" +
                    "FOREIGN KEY (id_pacient) REFERENCES pacienti(id)\n" +
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
                    "CREATE OR REPLACE TRIGGER programari_trigger\n" +
                            "BEFORE INSERT ON PROGRAMARI\n" +
                            "FOR EACH ROW\n" +
                            "BEGIN\n" +
                            "  SELECT programari_seq.NEXTVAL INTO :NEW.ID FROM DUAL;\n" +
                            "END;";
            statement.executeUpdate(script);
            System.out.println("Script executed successfully.");
        } catch (SQLException e) {
            System.err.println("Error executing script: " + e.getErrorCode() + " - " + e.getMessage());
        }
    }
    public Programari selectProgById(int id) throws SQLException {
        Programari prog = new Programari();
        DB conn = new DB();
        Connection connection = conn.getCon();

        Statement statement = connection.createStatement();
        String script =
                "SELECT * FROM PROGRAMARI WHERE ID=?";
        PreparedStatement stmt = connection.prepareStatement(script);
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            prog.id = resultSet.getInt("id");
            prog.id_pacient = resultSet.getInt("id_pacient");
            prog.id_medic = resultSet.getInt("id_medic");
            prog.date = resultSet.getDate("data");
            prog.ora = resultSet.getString("ora");
        }

        return prog;
    }
    public Programari(){
        createSequens();
        createTable();
        createTrigger();
    }

    public static void main(String[] args) {
        Programari programari = new Programari();

    }
}
