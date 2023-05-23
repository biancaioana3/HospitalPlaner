package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PacientDashboard extends JDialog{
    private JScrollPane mainScrollPane;
    private JPanel  panel1;
    private JLabel nume;
    private JLabel telefon;
    private JLabel adresa;
    private JButton cencel;
    private JButton addProgramare;
    private JTextArea textProgramari;
    public int user_id;
    public int pacient_id;

    public PacientDashboard(JDialog parent, int user_id){
        super(parent);
        this.user_id = user_id;
        setTitle("Login");
        JScrollPane mainScrollPane = new JScrollPane(panel1);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setContentPane(mainScrollPane);
        setMaximumSize(new Dimension(450, 474));
        setSize(700, 650);

        Pacienti pacient = selectPacient(user_id);
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
                    AdaugaProgramare adaugaProgramare = new AdaugaProgramare(null,pacient_id);
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

            JTextArea resultPanel = new JTextArea();
            resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));


            while (resultSet.next()){

                int id = resultSet.getInt("id");
                int id_medic = resultSet.getInt("id_medic");
                Medici medic = new Medici();
                medic = medic.selectMedicById(id_medic);
                String ora = resultSet.getString("ora");
                Date data = resultSet.getDate("data");
                Pacienti pacient = new Pacienti();
                pacient = pacient.selectPacientById(pacient_id);
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
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
            panel1.add(scrollPane);
            textProgramari.setLayout(new BorderLayout());
            textProgramari.add(scrollPane, BorderLayout.CENTER);

            resultSet.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Pacienti selectPacient( int user_id){
        Pacienti pacient = new Pacienti();

        try {
            DB con = new DB();
            Connection conn = con.getCon();
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM PACIENTI WHERE ID_USER = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                pacient = new Pacienti();
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
        PacientDashboard pacient =  new PacientDashboard(null, 2);
    }
}