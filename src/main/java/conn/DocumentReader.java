package conn;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import javax.imageio.ImageIO;
import org.apache.log4j.BasicConfigurator;

public class DocumentReader extends JDialog {
    private JButton chooseButton;
    private JButton saveButton;
    private JButton myDocuments;
    private JTextArea textArea;
    private JLabel imageLabel;
    private File selectedFile;
    private File selectedImageFile;
    public int id_pacient;
    public int document_type;

    public DocumentReader(JFrame parent, int id_pacient, int document_type) {
        BasicConfigurator.configure(); // Configurare minimă pentru Log4j
        this.id_pacient=id_pacient;
        this.document_type=document_type;
        setTitle("Document Reader");
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);

        chooseButton = new JButton("Choose Document");
        saveButton = new JButton("Save Document");
        myDocuments = new JButton("Documentele mele");

        textArea = new JTextArea();
        imageLabel = new JLabel();

        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Documents", "txt", "doc", "docx", "pdf"));
                int result = fileChooser.showOpenDialog(DocumentReader.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    String documentName = selectedFile.getName();
                    readDocument(selectedFile, documentName);
                }
            }
        });
        myDocuments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    DocumentList documentList = new DocumentList(null, id_pacient);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    try {
                        saveDocument(selectedFile);
                        JOptionPane.showMessageDialog(DocumentReader.this, "Document saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);


                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DocumentReader.this, "Error saving document!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(DocumentReader.this, "No document selected!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(chooseButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(myDocuments);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        contentPanel.add(imageLabel, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void insertDocument(String documentName, String text) throws SQLException {


        DB connection = new DB();
        Connection conn = connection.getCon();
        String sql = "INSERT INTO documente (id_pacient, document_type, path, text) " +
                " VALUES (?,?,?,?) ";
        CallableStatement callableStatement = conn.prepareCall(sql);
        callableStatement.setInt(1, id_pacient);
        callableStatement.setInt(2, document_type);
        callableStatement.setString(3, documentName);
        callableStatement.setString(4, text);

        callableStatement.execute();


        callableStatement.close();
        conn.close();

    }

    private void readDocument(File file, String documentName) {
        try {
            if (file.getName().endsWith(".pdf")) {
                String text = readPDF(file);
                textArea.setText(documentName);
                insertDocument(documentName, text);
            } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                textArea.setText("");
                displayImage(file);
                insertDocument(documentName, "");

            } else {
                String text = readTextDocument(file);
                textArea.setText(documentName);
                insertDocument(documentName, text);

            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading document!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String readPDF(File file) throws IOException {
        try (InputStream is = new FileInputStream(file);
             PDDocument document = PDDocument.load(is)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            return text;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String readTextDocument(File file) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            text.append(line).append("\n");
        }
        reader.close();
        return text.toString();
    }

    private void displayImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying image!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDocument(File file) throws IOException {
        File documentsDir = new File("documents");
        if (!documentsDir.exists()) {
            documentsDir.mkdir();
        }

        String fileName = file.getName();
        File outputFile = new File(documentsDir, fileName);
        try (FileWriter writer = new FileWriter(outputFile);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(textArea.getText());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DocumentReader(null, 1, 1);
            }
        });
    }
}
