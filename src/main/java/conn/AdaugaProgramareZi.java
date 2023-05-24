package conn;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdaugaProgramareZi extends JDialog{
    private JPanel dataPanel;
    private JButton submit;
    private JDateChooser JtData;
    public int specializare;
    public int id_medic;
    public Date data;
    public int id_pacient;
    public int id_programare;
    public AdaugaProgramareZi(JFrame parent, int id_specializare,int id_pacient,int id_medic, int id_programare) throws SQLException {
        super(parent);
        this.specializare=id_specializare;
        this.id_medic = id_medic;
        this.id_pacient = id_pacient;
        this.id_programare = id_programare;
        setTitle("Create medic account");
        setContentPane(dataPanel);
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JtData = new JDateChooser();
        dataPanel.add(JtData);


        dataPanel.add(submit);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if(id_programare != 0){
            Programari prog = new Programari();
            prog = prog.selectProgById(id_programare);
            JtData.setDate(prog.date);
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AdaugaProgramOra adaugaProgramOra = new AdaugaProgramOra(null, id_specializare, id_medic,id_pacient, getData(), id_programare);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }else if (id_programare == 0){
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose();
                        AdaugaProgramOra adaugaProgramOra = new AdaugaProgramOra(null, id_specializare, id_medic,id_pacient, getData(), 0);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        setVisible(true);
    }

    private Date getData() throws SQLException {
        Date data = JtData.getDate();
        System.out.println(data);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        String dataFormatata = dateFormat.format(data);
        System.out.println(dataFormatata); // Afișează data formatată în consolă

        try {
            return dateFormat.parse(dataFormatata);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws SQLException {
        AdaugaProgramareZi adaugaProgramareMedic = new AdaugaProgramareZi(null, 4, 1,1,1);
    }
}
