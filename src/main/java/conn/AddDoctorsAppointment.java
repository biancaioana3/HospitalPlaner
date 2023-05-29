package conn;

import org.controlsfx.control.PropertySheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddDoctorsAppointment extends JDialog{
    private JComboBox<String> comboBox;
    private JPanel panel1;
    private JPanel addProgPanel;
    private JButton submit;
    public int specializare;
    public int id_pacient;
    public int id_programare;
    public AddDoctorsAppointment(JFrame parent, int id_specializare, int id_pacient, int id_programare) throws SQLException {
        super(parent);
        this.specializare=id_specializare;
        this.id_pacient = id_pacient;
        this.id_programare = id_programare;
        setTitle("Create medic account");
        setContentPane(addProgPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        addProgPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        addProgPanel.setLayout(new BoxLayout(addProgPanel, BoxLayout.Y_AXIS));

        JPanel comboBoxPanel = createComboBoxPanel("Medic", MediciComboBox());
        addProgPanel.add(comboBoxPanel);

        addProgPanel.add(submit);

        if(id_programare != 0){
            Appointments prog = new Appointments();
            prog = prog.selectAppointmentById(id_programare);
            Doctors medic = new Doctors();
            medic = medic.selectDoctorById(prog.id_medic);

            comboBox.setSelectedItem(medic.nume);
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AddDayProgramming addDayProgramming = new AddDayProgramming(null, id_specializare,id_pacient, getMedic(), id_programare);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        } else if(id_programare == 0){
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AddDayProgramming addDayProgramming = new AddDayProgramming(null, id_specializare,id_pacient, getMedic(),0);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        setVisible(true);
    }

    private int getMedic() throws SQLException {
        String medic = (String) comboBox.getSelectedItem();
        System.out.println(medic);
        System.out.println(medic);

        // Apoi poți utiliza numele medicului pentru a căuta ID-ul în baza de date
        int medic_id = 0;
        DB connection = new DB();
        Connection conn = connection.getCon();
        String query = "SELECT id FROM MEDICI WHERE  nume || ' ' || prenume = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, medic);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            medic_id = resultSet.getInt("id");
        }

        resultSet.close();
        preparedStatement.close();
        conn.close();

        return medic_id;
    }


    private JPanel createComboBoxPanel(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(comboBox);
        return panel;
    }
    public JComboBox<String> MediciComboBox() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        comboBox = new JComboBox<>();

        try {
            DB con = new DB();
            connection = con.getCon();
            statement = connection.createStatement();
            String query = "SELECT nume || ' ' || prenume AS nume_complet FROM MEDICI WHERE ID_SECTIE = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, specializare);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String numeComplet = resultSet.getString("nume_complet");
                System.out.println(numeComplet);
                comboBox.addItem(numeComplet);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }

        return comboBox;
    }

    public static void main(String[] args) throws SQLException {
        AddDoctorsAppointment addDoctorsAppointment = new AddDoctorsAppointment(null, 4, 1, 1);
    }
}
