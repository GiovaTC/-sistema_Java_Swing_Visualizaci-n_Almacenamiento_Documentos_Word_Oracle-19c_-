package com.example;

import oracle.jdbc.proxy.annotation.Pre;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DocumentoDAO {

    private Connection conn;

    public DocumentoDAO(Connection conn) {
        this.conn = conn;
    }

    public void guardarDocumento(String nombre, String ruta) {
        String sql = "INSERT INTO DOCUMENTOS_WORD (NOMBRE,ARCHIVO) VALUES(?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(new File(ruta))) {

            ps.setString(1, nombre);
            ps.setBinaryStream(2, fis, fis.available());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
