package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JPanel loginPanel;
    private javax.swing.JPanel JPanel;
    private JLabel email;
    private JTextField JEmail;
    private JLabel parola;
    private JPasswordField JParola;
    private JButton cencelButton;
    private JButton loginButton;
    public LoginForm(JDialog parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = JEmail.getText();
                String parola = String.valueOf(JParola.getPassword());

                try {
                    user = getAuthenticatedUser(email, parola);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (user!= null){
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Email or Password invalid!", "Try again", JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        cencelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public User user;
    private User getAuthenticatedUser(String email, String parola) throws SQLException {
        User user = null;

        DB connection = new DB();
       try {
           Connection conn = connection.getCon();
           Statement stmt = conn.createStatement();
           String sql = "SELECT * FROM USERS WHERE EMAIL = ? AND PAROLA = ?";
           PreparedStatement preparedStatement = conn.prepareStatement(sql);
           preparedStatement.setString(1, email);
           preparedStatement.setString(2, parola);

           ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()){
               user = new User();
               user.nume = resultSet.getString("nume");
               user.email = resultSet.getString("email");
               user.telefon = resultSet.getString("telefon");
               user.id = resultSet.getInt("id");
               user.isMedic = resultSet.getInt("is_Medic");
           }
           stmt.close();
           conn.close();

       }catch (Exception e){
            e.printStackTrace();
       }

        return user;


    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if (user!= null){
            System.out.println("Autentificare cu succes: " + user.nume);
            System.out.println("Id: " + user.id);
            System.out.println("Email: " +user.email);
            System.out.println("Telefon: " + user.telefon);

            if(user.isMedic == 1){
                int medic_id = user.id;
                MedicDashboard medicDashboard = new MedicDashboard(null, medic_id);
            }else if (user.isMedic == 0){
                PacientDashboard pacientDashboard = new PacientDashboard();
            }

        }else{
            System.out.println("Authentication canceled!");
        }
    }
}
