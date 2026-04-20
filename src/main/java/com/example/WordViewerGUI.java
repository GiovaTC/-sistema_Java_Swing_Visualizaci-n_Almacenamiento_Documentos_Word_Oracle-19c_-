package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class WordViewerGUI extends JFrame {

    private JTextArea textArea;

    public WordViewerGUI() {
        setTitle("Visor de Word + Oracle");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);

        JButton btnCargar = new JButton("Cargar Word");

        btnCargar.addActionListener((ActionEvent e) -> cargarDocumento());

        add(scroll, BorderLayout.CENTER);
        add(btnCargar, BorderLayout.SOUTH);
    }

    private void cargarDocumento() {
        JFileChooser fileChooser = new JFileChooser();
        int opcion = fileChooser.showOpenDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            WordService service = new WordService();
            String contenido = service.leerDocumento(archivo.getAbsolutePath());

            textArea.setText(contenido);

            guardarEnBD(archivo);
        }
    }

    private void guardarEnBD(File archivo) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//localhost:1521/orcl",
                    "system",
                    "Tapiero123"
            );

            DocumentoDAO dao = new DocumentoDAO(conn);
            dao.guardarDocumento(archivo.getName(), archivo.getAbsolutePath());

            JOptionPane.showMessageDialog(this, "Documento guardado en ORACLE 19C .");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordViewerGUI().setVisible(true));
    }
}
    