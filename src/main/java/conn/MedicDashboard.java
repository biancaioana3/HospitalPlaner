package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MedicDashboard extends JDialog{
    private JPanel panel1;
    private JLabel numeMedic;
    private JLabel specializare;
    private JLabel telefonMedic;
    private JTextArea textPacient;
    private JButton addProgramare;
    private JButton cencel;
    public int medic_id;

    public MedicDashboard(JDialog parent, int medic_id){
        super(parent);
        this.medic_id = medic_id;
        setTitle("Login");
        setContentPane(panel1);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        Medici medic = selectMedici(medic_id);
        numeMedic.setText(medic.nume + " " + medic.prenume);
        String specializareMedic = getMedicSpecializare(medic.specializare);
        System.out.println(specializareMedic);
        specializare.setText(specializareMedic);
        textPacient.setEditable(false);
        telefonMedic.setText(medic.telefon);
        pacientSelect();
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


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

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                int id_pacient = resultSet.getInt("id_pacient");
                String ora = resultSet.getString("ora");
                Date data = resultSet.getDate("data");


                String resultRow = "ID: " + id + " ID pacient: " + id_pacient + " Ora: " + ora + " Data: " + data;
                JLabel resultLabel = new JLabel(resultRow);
                JButton fisaPacientButton = new JButton("Fisa pacient");

                fisaPacientButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Fisa pacient pentru ID: " + id_pacient);
                    }
                });

                JPanel rowPanel = new JPanel(new BorderLayout());
                rowPanel.add(resultLabel, BorderLayout.CENTER);
                rowPanel.add(fisaPacientButton, BorderLayout.EAST);

                resultPanel.add(rowPanel);

            }
            JScrollPane scrollPane = new JScrollPane(resultPanel);
            textPacient.setLayout(new BorderLayout());
            textPacient.add(scrollPane, BorderLayout.CENTER);

            resultSet.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Sectii sectie = new Sectii();
    private String getMedicSpecializare(int id) {
        String specializare = " ";
        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM SECTII WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                sectie = new Sectii();
                sectie.nume = resultSet.getString("nume");
            }
            specializare = sectie.nume;
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specializare;
    }

    Medici medic = new Medici();
    public Medici selectMedici(int medic_id){
        Medici medic = new Medici();

        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM MEDICI WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, medic_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                medic = new Medici();
                medic.specializare = resultSet.getInt("id_sectie");
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
        MedicDashboard medicDashboard = new MedicDashboard(null, 3);
    }
}
