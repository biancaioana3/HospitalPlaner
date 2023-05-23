package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdaugaProgramare extends JDialog {
    private JComboBox<String> comboBox;
    private JPanel panel1;
    private JPanel addProgPanel;
    private JButton submit;
    public int id_pacient;
    public AdaugaProgramare(JFrame parent, int id_pacient) throws SQLException {
        super(parent);
        this.id_pacient=id_pacient;
        setTitle("Create medic account");
        setContentPane(addProgPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        addProgPanel.setLayout(new BoxLayout(addProgPanel, BoxLayout.Y_AXIS));

        JPanel comboBoxPanel = createComboBoxPanel("Specializare", SpecializariComboBox());
        addProgPanel.add(comboBoxPanel);

        addProgPanel.add(submit);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dispose();
                    AdaugaProgramareMedic adaugaProgramareMedic = new AdaugaProgramareMedic(null, getSpecializare(), id_pacient);

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    private int getSpecializare() throws SQLException {
        int specializare = comboBox.getSelectedIndex() +1;
        return  specializare;
    }

    private JPanel createComboBoxPanel(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(comboBox);
        return panel;
    }
    public JComboBox<String> SpecializariComboBox() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        comboBox = new JComboBox<>();

        try {
            DB con = new DB();
            connection = con.getCon();

            String query = "SELECT NUME FROM SECTII";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nume = resultSet.getString("NUME");
                comboBox.addItem(nume);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return comboBox;
    }

    public static void main(String[] args) throws SQLException {
        AdaugaProgramare adaugaProgramare = new AdaugaProgramare(null, 1);
    }
}
