package com.example;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

public class WordService {

    public String leerDocumento(String ruta) {
        StringBuilder contenido = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(ruta);
             XWPFDocument doc = new XWPFDocument(fis)) {

            doc.getParagraphs().forEach(p ->
                    contenido.append(p.getText()).append("\n")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return  contenido.toString();
    }
}
