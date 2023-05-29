package conn;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FisaPacient {
    public static void generateFisaPacient(String fileName, int pacient_id) {

        DB con = new DB();
        try (
                Connection conn = con.getCon();
                Statement stmt = conn.createStatement()) {

            String sqlScript = "DECLARE\n" +
                    "  v_fisier UTL_FILE.FILE_TYPE;\n" +
                    "  v_nume_fisier VARCHAR2(100) := '"+fileName+"';\n" +
                    "  v_separator VARCHAR2(10) := ',';\n" +
                    "  v_nume VARCHAR2(100);\n" +
                    "  v_prenume VARCHAR2(100);\n" +
                    "  v_varsta NUMBER;\n" +
                    "  v_data_nasterii DATE;\n" +
                    "  v_gen NUMBER;\n" +
                    "  v_adresa VARCHAR2(100);\n" +
                    "  v_telefon VARCHAR2(20);\n" +
                    "BEGIN\n" +
                    "  -- Populăm variabilele cu informațiile pacientului din baza de date\n" +
                    "  SELECT nume, prenume, varsta, data_nasterii, gen, adresa, telefon\n" +
                    "  INTO v_nume, v_prenume, v_varsta, v_data_nasterii, v_gen, v_adresa, v_telefon\n" +
                    "  FROM pacienti\n" +
                    "  WHERE id = "+pacient_id+";" +
                    "\n" +
                    "  v_fisier := UTL_FILE.FOPEN('MYDIR', v_nume_fisier, 'W');\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'FISĂ PACIENT');\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, '--------------------------------------------------------------');\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Nume: ' || v_nume);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Prenume: ' || v_prenume);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Varsta: ' || v_varsta);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Data nasterii: ' || TO_CHAR(v_data_nasterii, 'DD/MM/YYYY'));\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Gen: ' || CASE v_gen WHEN 1 THEN 'Feminin' WHEN 2 THEN 'Masculin' ELSE 'Necunoscut' END);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Adresa: ' || v_adresa);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, 'Telefon: ' || v_telefon);\n" +
                    "    UTL_FILE.PUTF(v_fisier, CHR(10));\n" +
                    "  UTL_FILE.PUTF(v_fisier, '--------------------------------------------------------------');\n" +
                    "  UTL_FILE.FCLOSE(v_fisier);\n" +
                    "\n" +
                    "  DBMS_OUTPUT.PUT_LINE('Fișierul ' || v_nume_fisier || ' a fost generat cu succes!');\n" +
                    "EXCEPTION\n" +
                    "  WHEN NO_DATA_FOUND THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Nu s-a găsit pacientul cu ID-ul specificat.');\n" +
                    "  WHEN UTL_FILE.INVALID_OPERATION THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Eroare: Operatiune de fisier invalida');\n" +
                    "  WHEN UTL_FILE.INVALID_PATH THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Eroare: Calea fisierului este invalida');\n" +
                    "  WHEN UTL_FILE.WRITE_ERROR THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Eroare: Eroare la scrierea in fisier');\n" +
                    "  WHEN UTL_FILE.INVALID_MODE THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Eroare: Modul fisierului este invalid');\n" +
                    "  WHEN UTL_FILE.INVALID_FILEHANDLE THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('Eroare: Handle-ul fisierului este invalid');\n" +
                    "  WHEN OTHERS THEN\n" +
                    "    DBMS_OUTPUT.PUT_LINE('A aparut o eroare: ' || SQLERRM);\n" +
                    "END;";
            stmt.execute(sqlScript);

            JOptionPane.showMessageDialog(null, "Fișa Pacientului generata cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            System.out.println("A apărut o eroare SQL: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws ParseException {
        String fileName = "fisaPacient.txt";

        generateFisaPacient(fileName, 1);
    }
}
