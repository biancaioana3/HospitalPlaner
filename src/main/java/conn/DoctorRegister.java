package conn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DoctorRegister extends JDialog {
    public int id;
    private JComboBox<String> luni;
    private JComboBox<String> marti;
    private JComboBox<String> miercuri;
    private JComboBox<String> joi;
    private JComboBox<String> vineri;
    private JPanel registerPanel;
    private JButton submit;
    private JComboBox<String> comboBox;
    public Doctors doctors;

    public DoctorRegister(JFrame parent, int id) throws SQLException {
        super(parent);
        this.id=id;
        setTitle("Create medic account");
        setContentPane(registerPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        JPanel comboBoxPanel = createComboBoxPanel("Specializare", SpecializariComboBox());
        registerPanel.add(comboBoxPanel);

        JPanel luniPanel = createComboBoxPanel("Luni", luni);
        registerPanel.add(luniPanel);

        JPanel martiPanel = createComboBoxPanel("Marti", marti);
        registerPanel.add(martiPanel);

        JPanel miercuriPanel = createComboBoxPanel("Miercuri", miercuri);
        registerPanel.add(miercuriPanel);

        JPanel joiPanel = createComboBoxPanel("Joi", joi);
        registerPanel.add(joiPanel);

        JPanel vineriPanel = createComboBoxPanel("Vineri", vineri);
        registerPanel.add(vineriPanel);

        registerPanel.add(submit);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerMedic();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        setVisible(true);
    }

    private void registerMedic() throws SQLException {
        int specializare = comboBox.getSelectedIndex() +1;
        int pLuni = luni.getSelectedIndex();
        int pMarti = marti.getSelectedIndex();
        int pMiercuri = miercuri.getSelectedIndex();
        int pJoi = joi.getSelectedIndex();
        int pVineri = vineri.getSelectedIndex();

        doctors = addMedicToDB(specializare,pLuni,pMarti,pMiercuri,pJoi, pVineri);

        if(pLuni == 0 || pMarti == 0 || pMiercuri == 0 || pJoi == 0 || pVineri == 0 ){
            JOptionPane.showMessageDialog(this, "Te rugam completeaza toate campurile!", "Mai incearca o data!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(doctors != null){
            System.out.println(doctors);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Eroare in inregistrarea noului medic" , "Mai incearca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Doctors addMedicToDB(int specializare, int pLuni, int pMarti, int pMiercuri, int pJoi, int pVineri) throws SQLException {

        Doctors doctors = null;

        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql ="UPDATE MEDICI SET ID_SECTIE =? WHERE ID_USER =?";
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setInt(1, specializare);
        callableStatement.setInt(2,id);

        callableStatement.execute();


        Doctors medicDate = new Doctors();
        int idmedic = Integer.parseInt(medicDate.selectDoctorByUserId(id, "id"));

        if (idmedic > 0) {
            doctors = new Doctors();
            doctors.id = idmedic;
            doctors.specializare = doctors.getDoctorSpecialization(specializare);
            doctors.luni = pLuni;
            doctors.marti = pMarti;
            doctors.miercuri = pMiercuri;
            doctors.joi = pJoi;
            doctors.vineri = pVineri;
        } else {
            throw new SQLException("Failed to retrieve the last inserted ID.");
        }

        callableStatement.close();

        String sqlProgram ="INSERT INTO PROGRAM (id, luni, marti, miercuri, joi, vineri) VALUES (?,?,?,?,?,?) " ;
        CallableStatement callableStatementProgram = conn.prepareCall(sqlProgram);
        callableStatementProgram.setInt(1, idmedic);
        callableStatementProgram.setInt(2, pLuni);
        callableStatementProgram.setInt(3, pMarti);
        callableStatementProgram.setInt(4, pMiercuri);
        callableStatementProgram.setInt(5, pJoi);
        callableStatementProgram.setInt(6, pVineri);

        callableStatementProgram.execute();


        callableStatementProgram.close();
        conn.close();
        return doctors;
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
        DoctorRegister doctorRegister = new DoctorRegister(null, 14);
    }
}
