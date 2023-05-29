package conn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

public class DocumentList extends JDialog {
    private JList<String> documentList;
    private JButton downloadButton;
    private JButton addDocument;
    private JButton cencel;

    private JPanel dataPanel;
    public int pacient_id;

    public DocumentList(JFrame parent, int pacient_id) throws SQLException {
        this.pacient_id = pacient_id;
        setTitle("Document List");
        setMaximumSize(new Dimension(450, 474));
        setSize(500, 450);


        documentList = new JList<>();
        downloadButton = new JButton("Download");
        addDocument = new JButton("Adauga Document");
        cencel = new JButton("Cencel");
        cencel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        addDocument.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AddDocument addDoc = new AddDocument(null,pacient_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = documentList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedDocument = documentList.getSelectedValue();
                    downloadDocument(selectedDocument);
                } else {
                    JOptionPane.showMessageDialog(DocumentList.this, "No document selected!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(downloadButton);
        buttonPanel.add(addDocument);
        buttonPanel.add(cencel);

        dataPanel = new JPanel(new BorderLayout());
        dataPanel.add(new JScrollPane(documentList), BorderLayout.CENTER);
        dataPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(dataPanel);

        populateDocumentList();
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void populateDocumentList() throws SQLException {
        DefaultListModel<String> listModel = new DefaultListModel<>();
            DB con = new DB();
            Connection connection = con.getCon();
            String sql = "SELECT path FROM documente WHERE id_pacient = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pacient_id); // înlocuiește "id_pacient" cu variabila ta corespunzătoare
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String path = resultSet.getString("path");
                listModel.addElement(path);
            }

        documentList.setModel(listModel);
    }



    private void downloadDocument(String documentName) {

        File sourceFile = new File("documents/" + documentName);
        File destinationFile = new File("downloads/" + documentName);

        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            JOptionPane.showMessageDialog(this, "Document downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error downloading document!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new DocumentList(null, 1);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
