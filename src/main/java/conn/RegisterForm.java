package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterForm  extends JDialog{
    public int id;
    private JTextField rNume;
    private JTextField rPrenume;
    private JTextField rEmail;
    private JTextField rTelefon;
    private JPasswordField rParola;
    private JPasswordField rConfirmaParola;
    private JButton inregistrareCont;
    private JButton cencel;
    private JPanel registerPanel;
    private JRadioButton isMedic;
    private JRadioButton isNotMedic;
    public User user;

    public RegisterForm(JFrame parent){
        super(parent);
        setTitle("Create new account");
        setContentPane(registerPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        inregistrareCont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser();
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

    private void registerUser() throws SQLException {
        int medic = 0;
        String nume = rNume.getText();
        String email = rEmail.getText();
        String prenume = rPrenume.getText();
        String telefon = rTelefon.getText();
        String parola = String.valueOf(rParola.getPassword());
        String confirmaParola = String.valueOf(rConfirmaParola.getPassword());
        if(isMedic.isSelected()){
            medic = 1;
        }else if( isNotMedic.isSelected()){
            medic = 0;
        }

        if(nume.isEmpty() || prenume.isEmpty() || email.isEmpty() || telefon.isEmpty() || parola.isEmpty() || confirmaParola.isEmpty() || !(isNotMedic.isSelected() || isMedic.isSelected()) || (isMedic.isSelected() && isNotMedic.isSelected())){
            JOptionPane.showMessageDialog(this, "Te rugam completeaza toate campurile!", "Mai incearca o data!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!parola.equals(confirmaParola)){
            JOptionPane.showMessageDialog(this, "Parola nu se potriveste", "Mai incearca o data!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        user = addUserToDB(nume,email,prenume,telefon,parola, medic);

        if(user != null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Eroare in inregistrarea noului user" , "Mai incearca", JOptionPane.ERROR_MESSAGE);
        }
    }

    private User addUserToDB(String nume, String email, String prenume, String telefon, String parola, int medic) throws SQLException {
        User user = null;

        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql = "DECLARE " +
                "    last_insert_id NUMBER; " +
                "BEGIN " +
                "    INSERT INTO users (nume, prenume, telefon, email, parola, ismedic) " +
                "    VALUES (?,?,?,?,?,?) " +
                "    RETURNING id INTO last_insert_id; " +
                "    ? := last_insert_id; " +
                "END;";
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setString(1, nume);
        callableStatement.setString(2, prenume);
        callableStatement.setString(3, telefon);
        callableStatement.setString(4, email);
        callableStatement.setString(5, parola);
        callableStatement.setString(6, String.valueOf(medic));
        callableStatement.registerOutParameter(7, Types.NUMERIC);

        callableStatement.execute();

        int lastInsertId = callableStatement.getInt(7);

        if (lastInsertId > 0) {
            user = new User();
            user.id = lastInsertId;
            user.nume = nume;
            user.prenume = prenume;
            user.email = email;
            user.telefon = telefon;
            user.parola = parola;
            user.isMedic = medic;
        } else {
            throw new SQLException("Failed to retrieve the last inserted ID.");
        }

        callableStatement.close();
        conn.close();
        return user;
    }


    public static void main(String[] args) throws SQLException {
        RegisterForm myForm = new RegisterForm(null);
        User user = myForm.user;

        if(user != null){
            if(user.isMedic == 1){
                System.out.println(user.id);
                MedicRegister medicRegister = new MedicRegister(null, user.id);
            }else if(user.isMedic == 0){
                PacientRegister pacientRegister = new PacientRegister(null, user.id);
            }
            System.out.println("Successful registration of: "+ user.nume);
        } else{
            System.out.println("Registration canceled!");
        }
    }
}
