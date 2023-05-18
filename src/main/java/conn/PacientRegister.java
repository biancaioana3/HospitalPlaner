package conn;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PacientRegister extends JDialog{
    private JPanel panel1;
    private JTextField JtAdresa;
    private JDateChooser  JtDataNasteri;
    private JSpinner JsVarsta;
    private JComboBox JcGen;
    private JButton JbSave;
    private JPanel pacientRegister;

    public PacientRegister(JFrame parent, int id){
        super(parent);
        setTitle("Create new account");
        setContentPane(pacientRegister);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setVisible(true);
        JbSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerPacient();
            }
        });
    }

    private void registerPacient() {
    }

    public static void main(String[] args) {
        PacientRegister pacient = new PacientRegister(null, 14);
    }
}
