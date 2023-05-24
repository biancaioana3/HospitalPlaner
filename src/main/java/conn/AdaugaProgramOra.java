package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdaugaProgramOra extends JDialog{
    private JComboBox<String> comboBox;
    public int specializare;
    public int id_medic;
    public Date date;
    private JPanel ProgramPanel;
    private JButton submit;
    private JButton selecteazaAltaDataButton;
    public int id_pacient;
    public String ora;
    public int id_programare;

    public AdaugaProgramOra(JFrame parent, int id_specializare, int id_medic,int id_pacient, Date date, int id_programare) throws SQLException {
        super(parent);
        this.specializare=id_specializare;
        this.id_medic = id_medic;
        this.date = date;
        this.id_pacient = id_pacient;
        this.id_programare = id_programare;
        setTitle("Create medic account");
        setContentPane(ProgramPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        ProgramPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        ProgramPanel.setLayout(new BoxLayout(ProgramPanel, BoxLayout.Y_AXIS));

        JPanel comboBoxPanel = createComboBoxPanel("Ora", OraComboBox());
        ProgramPanel.add(comboBoxPanel);
        ProgramPanel.add(submit);
        ProgramPanel.add(selecteazaAltaDataButton);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if (id_programare != 0) {

            Programari prog = new Programari();
            prog = prog.selectProgById(id_programare);
            comboBox.setSelectedItem(prog.ora);
            selecteazaAltaDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AdaugaProgramareZi adaugaProgramareZi = new AdaugaProgramareZi(null, id_specializare,id_pacient ,id_medic, id_programare);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        Programari programari = updateProgramare();
                        if(programari != null){
                            JOptionPane.showMessageDialog(parent, "Programare modificata cu succes!" , "Va asteptam!", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(parent,
                                    "Programarea nu s-a putut modifica!" ,
                                    "Mai incearca",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } else if ( id_programare == 0){

            selecteazaAltaDataButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AdaugaProgramareZi adaugaProgramareZi = new AdaugaProgramareZi(null, id_specializare,id_pacient ,id_medic, 0);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        Programari programari = addProgramare();
                        if(programari != null){
                            JOptionPane.showMessageDialog(parent, "Programare salvata cu succes!" , "Va asteptam!", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(parent,
                                    "Programarea nu s-a putut salva!" ,
                                    "Mai incearca",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        setVisible(true);
    }

    public Programari updateProgramare() throws SQLException {

        Programari prog = null;

        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql ="UPDATE PROGRAMARI SET ID_MEDIC =?, ID_PACIENT= ? , DATA=?, ORA=? WHERE ID =?";
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setInt(1, id_medic);
        callableStatement.setInt(2, id_medic);
        callableStatement.setDate(3, new java.sql.Date(date.getTime()));
        callableStatement.setString(4, getOra());
        callableStatement.setInt(5,id_programare);

        callableStatement.execute();


        if (id_programare > 0) {
            prog = new Programari();
            prog.id = id_programare;
            prog.id_medic = id_medic;
            prog.id_pacient = id_pacient;
            prog.date = date;
            prog.ora = ora;

        } else {
            throw new SQLException("Failed to retrieve the last inserted ID.");
        }

        callableStatement.close();

        conn.close();
        return prog;
    }
    public Programari addProgramare() throws SQLException {

        Programari programari = null;

        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql = "DECLARE " +
                "    last_insert_prog_id NUMBER; " +
                "BEGIN " +
                "    INSERT INTO PROGRAMARI (ID_MEDIC, ID_PACIENT, DATA, ORA) " +
                "    VALUES (?,?,?,?) " +
                "    RETURNING id INTO last_insert_prog_id; " +
                "    ? := last_insert_prog_id; " +
                "END;";

        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setInt(1, id_medic);
        callableStatement.setInt(2, id_pacient);
        callableStatement.setString(3, new SimpleDateFormat("dd-MM-yy").format(date));
        callableStatement.setString(4, getOra());
        callableStatement.registerOutParameter(5, Types.NUMERIC);

        callableStatement.execute();

        int lastInsertId = callableStatement.getInt(5);

        if (lastInsertId > 0) {
            programari = new Programari();
            programari.id = lastInsertId;
            programari.id_medic = id_medic;
            programari.id_pacient = id_pacient;
            programari.date = date;
            programari.ora = ora;
        } else {
            throw new SQLException("Failed to retrieve the last inserted ID.");
        }

        callableStatement.close();
        conn.close();
        return programari;
    }
    private String getOra() throws SQLException {
        this.ora = (String) comboBox.getSelectedItem();
        return  ora;
    }

    private JPanel createComboBoxPanel(String labelText, JComboBox<String> comboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(comboBox);
        return panel;
    }

    public String getZiuaSaptamanii(int dayOfWeek){
        String ziuaSaptamanii = null;
        if(dayOfWeek == 2){
            ziuaSaptamanii = "LUNI";
        } else if(dayOfWeek == 3){
            ziuaSaptamanii = "MARTI";
        }else if(dayOfWeek == 4){
            ziuaSaptamanii = "MIERCURI";
        }else if(dayOfWeek == 5){
            ziuaSaptamanii = "JOI";
        }else if(dayOfWeek == 6){
            ziuaSaptamanii = "VINERI";
        }
        System.out.println(ziuaSaptamanii);
        return ziuaSaptamanii;
    }

    public int getProgramOfDay(String day) throws SQLException{
        DB conn = new DB();
        Connection connection = conn.getCon();

        Statement statement = connection.createStatement();
        String script =
                "SELECT " +day+" FROM PROGRAM WHERE ID_MEDIC=?";
        PreparedStatement stmt = connection.prepareStatement(script);
        stmt.setInt(1, id_medic);
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(day);
        }
        return 0;

    }


    public JComboBox<String> OraComboBox() throws SQLException{
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        comboBox = new JComboBox<>();
        Medici medic = new Medici();
        String ziuaSaptamanii;
        int program = 0;

        try {
            DB con = new DB();
            connection = con.getCon();
            statement = connection.createStatement();

            String query = "SELECT ora FROM programari WHERE DATA = ?";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            String formattedDate = dateFormat.format(date);

            try {
                Date date = dateFormat.parse(formattedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                ziuaSaptamanii = getZiuaSaptamanii(dayOfWeek);
                program = getProgramOfDay(ziuaSaptamanii);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, formattedDate);
            resultSet = preparedStatement.executeQuery();
            List<String> oreProgramate = new ArrayList<>();

            while (resultSet.next()) {
                String oraProgramata = resultSet.getString("ora");
                oreProgramate.add(oraProgramata);
            }
            String[] oreDisponibile = new String[0];
            if(program == 1){
                oreDisponibile = new String[]{"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00"};
            } else if(program ==2 ){
                 oreDisponibile = new String[]{"15:00", "16:00", "17:00", "18:00", "19:00","20:00","21:00"};
            }

            for (String ora : oreDisponibile) {
                if (!oreProgramate.contains(ora)) {
                    comboBox.addItem(ora);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return comboBox;
    }


    public static void main(String[] args) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Date data = null;
        try {
            data = dateFormat.parse("23-05-05");
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdaugaProgramOra adaugaProgramOra= new AdaugaProgramOra(null, 4, 1, 1, data, 1);
    }
}
