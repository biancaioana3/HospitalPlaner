package conn;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class PatientRegister extends JDialog{
    public int id;
    public Patients patients;
    private JPanel panel1;
    private JTextField JtAdresa;
    private JDateChooser JtDataNasteri;
    private JSpinner JsVarsta;
    private JComboBox JcGen;
    private JButton JbSave;
    private JPanel pacientRegister;

    public PatientRegister(JFrame parent, int id){
        super(parent);
        this.id = id;
        setTitle("Create new account");
        setContentPane(pacientRegister);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JbSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerPacient();
                    PatientDashboard pacient = new PatientDashboard(null, id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void registerPacient() throws SQLException {
        String adresa = JtAdresa.getText();
        int varsta = (int) JsVarsta.getValue();
        Date datanasterii = JtDataNasteri.getDate();
        int gen = JcGen.getSelectedIndex();

        patients = addPacientToDB(adresa,varsta,datanasterii,gen);

        if(adresa.isEmpty() || varsta <= 0  || gen < 0){
            JOptionPane.showMessageDialog(this, "Te rugam completeaza toate campurile!", "Mai incearca o data!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(patients != null){
            System.out.println(patients);
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Eroare in inregistrarea noului pacient" , "Mai incearca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Patients addPacientToDB(String adresa, int varsta, Date datanasterii, int gen) throws SQLException {

        Patients pacient = null;

        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql ="UPDATE PACIENTI SET adresa =?, data_nasterii= ? , varsta=?, gen=? WHERE ID_USER =?";
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setString(1, adresa);
        callableStatement.setDate(2, new java.sql.Date(datanasterii.getTime()));

        callableStatement.setInt(3, varsta);
        callableStatement.setInt(4, gen);
        callableStatement.setInt(5,id);

        callableStatement.execute();


        Patients pacientDate = new Patients();
        int id_pacient = Integer.parseInt(pacientDate.selectPatienttDyUserId(id, "id"));

        if (id_pacient > 0) {
            pacient = new Patients();
            pacient.id = id_pacient;
            pacient.adresa = adresa;
            pacient.varsta = varsta;
            pacient.gen = gen;

        } else {
            throw new SQLException("Failed to retrieve the last inserted ID.");
        }

        callableStatement.close();

        conn.close();
        return pacient;
    }

    public static void main(String[] args) {
        PatientRegister pacient = new PatientRegister(null, 14);
    }
}
