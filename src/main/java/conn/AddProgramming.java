package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddProgramming extends JDialog {
    private JComboBox<String> comboBox;
    private JPanel panel1;
    private JPanel addProgPanel;
    private JButton submit;
    private JButton cencel;
    public int id_patient;

    public int id_programare;
    public AddProgramming(JFrame parent, int id_patient, int id_programare) throws SQLException {
        super(parent);
        this.id_patient=id_patient;
        this.id_programare = id_programare;
        setTitle("Programare");
        setContentPane(addProgPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        addProgPanel.setLayout(new BoxLayout(addProgPanel, BoxLayout.Y_AXIS));

        JPanel comboBoxPanel = createComboBoxPanel("Specializare", SpecializationsComboBox());
        addProgPanel.add(comboBoxPanel);

        addProgPanel.add(submit);
        addProgPanel.add(cencel);

        if(id_programare != 0){
            Appointments prog = new Appointments();
            prog.selectAppointmentById(id_programare);
            comboBox.setSelectedItem(getSpecializationByIdProgramming());

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AddDoctorsAppointment addDoctorsAppointment = new AddDoctorsAppointment(null, getSpecialization(), id_patient, id_programare);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } else{
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AddDoctorsAppointment addDoctorsAppointment = new AddDoctorsAppointment(null, getSpecialization(), id_patient, 0);

                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        cencel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    private String getSpecializationByIdProgramming() throws SQLException {
        DB con = new DB();
        String nume = null;
        Connection conn = con.getCon();
        Statement stmt = conn.createStatement();
        String query = "SELECT sectii.nume\n" +
                "FROM programari\n" +
                "JOIN medici ON programari.id_medic = medici.id\n" +
                "JOIN sectii ON medici.id_sectie = sectii.id\n" +
                "WHERE programari.id = ? \n";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, id_programare);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
             nume = resultSet.getString("nume");
        }
        return nume;
    }

    private int getSpecialization() throws SQLException {
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
    public JComboBox<String> SpecializationsComboBox() throws SQLException {
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
        AddProgramming addProgramming = new AddProgramming(null, 1,1);
    }
}
