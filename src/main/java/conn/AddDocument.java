package conn;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddDocument extends JDialog {
    private JComboBox<String> comboBoxDocumentType;
    private JButton submit;
    private JPanel dataPanel;
    private JPanel panel;
    public int pacient_id;
    public AddDocument(JFrame parent, int pacient_id) throws SQLException {
        super(parent);
        this.pacient_id = pacient_id;
        setTitle("Adauga Document");
        setContentPane(dataPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450); // Set the preferred size of the dialog
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel comboBoxPanel = createComboBoxPanel("", DocumentComboBox());
        panel.add(comboBoxPanel);
        dataPanel.add(submit);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(getDocumentId());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private int getDocumentId() throws SQLException {
        int document_id = comboBoxDocumentType.getSelectedIndex() + 1;
        return document_id;
    }

    private JPanel createComboBoxPanel(String labelText, JComboBox<String> comboBoxDocumentType) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(comboBoxDocumentType);
        return panel;
    }

    public JComboBox<String> DocumentComboBox() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        comboBoxDocumentType = new JComboBox<>();

        try {
            DB con = new DB();
            connection = con.getCon();

            String query = "SELECT NUME FROM DOCUMENT_TYPE";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nume = resultSet.getString("NUME");
                comboBoxDocumentType.addItem(nume);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return comboBoxDocumentType;
    }

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddDocument addDocument = new AddDocument(null, 1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
