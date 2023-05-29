package conn;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVDay extends JDialog{
    private JPanel dataPanel;
    private JButton submit;
    private JDateChooser JtData;
    public int specializare;
    public int id_medic;
    public Date data;
    public int id_pacient;
    public int id_programare;
    public CSVDay(JFrame parent, int id_medic) throws SQLException {
        super(parent);
        this.id_medic = id_medic;
        setTitle("Selecteaza ziua");
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

            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Doctors doctor = new Doctors();
                        doctor = doctor.selectDoctorById(id_medic);
                        String CSVName= doctor.nume + "."+ doctor.prenume +"." + getStringData() +".csv";
                        CSVGenerator csv = new CSVGenerator();
                        csv.generateCSV(CSVName,id_medic,getData());
                        dispose();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

        setVisible(true);
    }

    private String getStringData(){
        Date data = JtData.getDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
        String dataFormatata = dateFormat.format(data);

        return dataFormatata;
    }

    private Date getData() throws SQLException {
        Date data = JtData.getDate();

        return data;
    }


    public static void main(String[] args) throws SQLException {
       CSVDay csv = new CSVDay(null, 1);
    }
}
