package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientDashboard extends JDialog{
    private JScrollPane mainScrollPane;
    private JPanel  panel1;
    private JLabel nume;
    private JLabel telefon;
    private JLabel adresa;
    private JButton cencel;
    private JButton addDocument;
    private JButton addProgramare;
    private JTextArea textProgramari;
    public int user_id;
    public int pacient_id;

    public PatientDashboard(JDialog parent, int user_id){
        super(parent);
        this.user_id = user_id;
        setTitle("Login");
        panel1.setLayout(new BorderLayout());
        JScrollPane mainScrollPane = new JScrollPane(panel1);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setContentPane(mainScrollPane);
        setMaximumSize(new Dimension(450, 474));
        setSize(700, 650);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        Patients pacient = selectPacient(user_id);
        this.pacient_id = pacient.id;
        nume.setText(pacient.nume + " " + pacient.prenume);
        textProgramari.setEditable(false);
        textProgramari.setLineWrap(true);
        textProgramari.setWrapStyleWord(false);
        textProgramari.setRows(5);
        textProgramari.setColumns(2);
        adresa.setText(pacient.adresa);
        telefon.setText(pacient.telefon);
        programariSelect();
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addProgramare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AddProgramming addProgramming = new AddProgramming(null,pacient_id, 0);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        addDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AddDocument document = new AddDocument(null, pacient_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cencel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        bottomPanel.add(addProgramare);
        bottomPanel.add(addDocument);
        bottomPanel.add(cencel);

        panel1.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void programariSelect() {
        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM PROGRAMARI WHERE ID_PACIENT = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, pacient_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));


            while (resultSet.next()){

                int id = resultSet.getInt("id");
                int id_medic = resultSet.getInt("id_medic");
                Doctors medic = new Doctors();
                medic = medic.selectDoctorById(id_medic);
                String ora = resultSet.getString("ora");
                Date data = resultSet.getDate("data");
                Patients pacient = new Patients();
                pacient = pacient.selectPatientById(pacient_id);
                String resultRow = "Nume Medic: " + medic.nume +
                        " " + medic.prenume + "\n" +
                        "Telefon: " + medic.telefon + "\n" +
                        "Sectie: " + medic.specializare + "\n"+
                        "Ora: " + ora  + "\n" +
                        "Data: " + data + "\n\n";
                JTextArea resultLabel = new JTextArea(resultRow);


                JButton fisaPacientButton = new JButton("Anuleaza programare");
                fisaPacientButton.setPreferredSize(new Dimension(200, 20));

                JButton updateProgramare = new JButton("Modifica programarea");
                updateProgramare.setPreferredSize(new Dimension(200, 20));

                fisaPacientButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Fisa pacient pentru ID: " + id);
                    }
                });

                updateProgramare.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Upadate programare: " + id);
                        try {
                            AddProgramming addProgramming = new AddProgramming(null,pacient_id, id);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
                JPanel buttonsPanel = new JPanel();
                buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                buttonsPanel.add(fisaPacientButton);
                buttonsPanel.add(updateProgramare);

                JPanel rowPanel = new JPanel(new BorderLayout());
                rowPanel.add(resultLabel, BorderLayout.WEST);
                rowPanel.add(buttonsPanel, BorderLayout.EAST);


                resultPanel.add(rowPanel);

            }
            JScrollPane scrollPane = new JScrollPane(resultPanel);
            panel1.add(scrollPane, BorderLayout.CENTER);

            resultSet.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Patients selectPacient(int user_id){
        Patients pacient = new Patients();

        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM PACIENTI WHERE ID_USER = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                pacient = new Patients();
                pacient.id_user = resultSet.getInt("id_user");
                pacient.nume = resultSet.getString("nume");
                pacient.id = resultSet.getInt("id");
                pacient.adresa = resultSet.getString("adresa");
                pacient.datanasterii = resultSet.getDate("data_nasterii");
                pacient.varsta = resultSet.getInt("varsta");
                pacient.gen = resultSet.getInt("gen");
                pacient.prenume = resultSet.getString("prenume");
                pacient.telefon = resultSet.getString("telefon");
            }
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacient;
    }


    public static void main(String[] args) {
        PatientDashboard pacient =  new PatientDashboard(null, 2);
    }
}
