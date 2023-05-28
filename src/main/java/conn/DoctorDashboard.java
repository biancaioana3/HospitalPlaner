package conn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorDashboard extends JDialog {
    private JPanel panel1;
    private JLabel numeMedic;
    private JLabel specializare;
    private JLabel telefonMedic;
    private JTextArea textPacient;
    private JButton cencel;
    public int user_id;
    public int medic_id;

    public DoctorDashboard(JDialog parent, int user_id) {
        super(parent);
        this.user_id = user_id;
        setTitle("Login");
        setContentPane(panel1);
        setMaximumSize(new Dimension(450, 474));
        setSize(700, 650);

        Doctors medic = selectMedici(user_id);
        this.medic_id = medic.id;
        numeMedic.setText(medic.nume + " " + medic.prenume);
        String specializareMedic = medic.specializare;
        System.out.println(specializareMedic);
        specializare.setText(specializareMedic);
        textPacient.setEditable(false);
        textPacient.setLineWrap(true);
        textPacient.setWrapStyleWord(false);
        textPacient.setRows(5);
        textPacient.setColumns(2);

        telefonMedic.setText(medic.telefon);
        pacientSelect();
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

    private void pacientSelect() {
        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM PROGRAMARI WHERE ID_MEDIC = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, medic_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int id_pacient = resultSet.getInt("id_pacient");
                String ora = resultSet.getString("ora");
                Date data = resultSet.getDate("data");
                Patients pacient = new Patients();
                pacient = pacient.selectPatientById(id_pacient);
                String resultRow = "Nume: " + pacient.nume +
                        " Prenume: " + pacient.prenume +
                        " Telefon: " + pacient.telefon + "\n" +
                        " Ora: " + ora +
                        " Data: " + data + "\n\n";
                JLabel resultLabel = new JLabel(resultRow);

                JButton fisaPacientButton = new JButton("Fisa pacient");
                fisaPacientButton.setPreferredSize(new Dimension(150, 20));

                fisaPacientButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Fisa pacient pentru ID: " + id_pacient);
                    }
                });

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                buttonsPanel.add(fisaPacientButton);

                JPanel rowPanel = new JPanel(new BorderLayout());
                rowPanel.add(resultLabel, BorderLayout.WEST);
                rowPanel.add(buttonsPanel, BorderLayout.EAST);

                resultPanel.add(rowPanel);
            }

            JScrollPane scrollPane = new JScrollPane(resultPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            textPacient.setLayout(new BorderLayout());
            textPacient.add(scrollPane, BorderLayout.CENTER);

            resultSet.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Doctors medic = new Doctors();

    public Doctors selectMedici(int user_id) {
        Doctors medic = new Doctors();

        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM MEDICI WHERE ID_USER = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                medic = new Doctors();
                medic.specializare = medic.getDoctorSpecialization(resultSet.getInt("id_sectie"));
                medic.id_user = resultSet.getInt("id_user");
                medic.nume = resultSet.getString("nume");
                medic.id = resultSet.getInt("id");
                medic.prenume = resultSet.getString("prenume");
                medic.telefon = resultSet.getString("telefon");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medic;
    }

    public static void main(String[] args) {
        DoctorDashboard doctorDashboard = new DoctorDashboard(null, 1);
    }
}
