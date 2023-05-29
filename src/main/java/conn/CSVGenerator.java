package conn;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVGenerator {
    public static void generateCSV(String fileName, int medicId, Date startDate) {

        DB con = new DB();
        try (
                Connection conn = con.getCon();
                Statement stmt = conn.createStatement()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedStartDate = dateFormat.format(startDate);

            String sqlScript = "DECLARE\n" +
                    "  v_fisier UTL_FILE.FILE_TYPE;\n" +
                    "  v_nume_fisier VARCHAR2(100) := '" + fileName + "';\n" +
                    "  v_separator VARCHAR2(10) := ',';\n" +
                    "BEGIN\n" +
                    "  v_fisier := UTL_FILE.FOPEN('MYDIR', v_nume_fisier, 'W');\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Ora,Data,Nume Pacient');\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  FOR programare IN (SELECT pr.ora, pr.data, INITCAP(p.nume || ' ' || p.prenume) AS nume_pacient\n" +
                    "                     FROM programari pr\n" +
                    "                     JOIN pacienti p ON pr.id_pacient = p.id\n" +
                    "                     WHERE pr.id_medic = " + medicId + " AND pr.data = TO_DATE('" + formattedStartDate + "', 'dd-MM-yyyy')\n" +
                    "                     ORDER BY pr.data, pr.ora) LOOP\n" +
                    "    UTL_FILE.PUTF(v_fisier, programare.ora || v_separator || programare.data || v_separator || programare.nume_pacient);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  END LOOP;\n" +
                    "  UTL_FILE.FCLOSE(v_fisier);\n" +
                    "END;";
            stmt.execute(sqlScript);

            JOptionPane.showMessageDialog(null, "Fișierul CSV a fost generat cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.out.println("A apărut o eroare SQL: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws ParseException {
        String fileName = "programari.csv";
        int medicId = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = dateFormat.parse("2023-05-05");

        generateCSV(fileName, medicId, startDate);
    }
}
