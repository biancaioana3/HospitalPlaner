package conn;

import java.sql.*;
import javax.swing.*;

public class SpecializariComboBox extends JFrame {
    private JComboBox<String> comboBox;

    public SpecializariComboBox() {

        // Declararea obiectelor de conexiune
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Conectarea la baza de date
            DB con = new DB();
            connection = con.getCon();

            // Crearea și executarea interogării SELECT pentru a obține numele din tabela SPECIALIZARI
            String query = "SELECT NUME FROM SPECIALIZARI";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // Adăugarea numelui într-un JComboBox
            comboBox = new JComboBox<>();
            while (resultSet.next()) {
                String nume = resultSet.getString("NUME");
                comboBox.addItem(nume);
            }

            // Configurarea ferestrei și afișarea JComboBox-ului
            add(comboBox);
            pack();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Închiderea resurselor
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SpecializariComboBox();
    }
}
