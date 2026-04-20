# -sistema_Java_Swing_Visualizaci-n_Almacenamiento_Documentos_Word_Oracle-19c_- :.

<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/1d62c095-2d1a-4a89-b980-a785cd5b74e8" />

```

🧩 Arquitectura del Sistema
Capas
GUI (Swing) → Interfaz gráfica para el usuario
Servicio → Procesamiento de archivos Word
DAO (JDBC) → Acceso a datos
Oracle 19c (BLOB) → Persistencia de archivos
Flujo
Usuario carga archivo Word
Se muestra en la GUI
Se guarda en Oracle .

🗄️ 1. Script Oracle 19c
CREATE TABLE DOCUMENTOS_WORD (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOMBRE VARCHAR2(200),
    ARCHIVO BLOB,
    FECHA_REGISTRO DATE DEFAULT SYSDATE
);
 
📦 2. Dependencias (Maven)
<dependencies>
    <!-- Apache POI para Word -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.5</version>
    </dependency>

    <!-- JDBC Oracle -->
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
        <version>21.9.0.0</version>
    </dependency>
</dependencies>

🧠 3. Servicio (Leer Word)
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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

        return contenido.toString();
    }
}

💾 4. DAO (Guardar en Oracle como BLOB)
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
        String sql = "INSERT INTO DOCUMENTOS_WORD (NOMBRE, ARCHIVO) VALUES (?, ?)";

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

🖥️ 5. GUI (Swing)
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
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "usuario",
                    "password"
            );

            DocumentoDAO dao = new DocumentoDAO(conn);
            dao.guardarDocumento(archivo.getName(), archivo.getAbsolutePath());

            JOptionPane.showMessageDialog(this, "Documento guardado en Oracle");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WordViewerGUI().setVisible(true));
    }
}

📄 6. Archivo Word de Ejemplo
Nombre del archivo:

ejemplo.docx

Contenido sugerido:

Título: Documento de Prueba

Este es un archivo Word de ejemplo.

Se utiliza para demostrar:
- Lectura con Apache POI
- Visualización en Java Swing
- Almacenamiento en Oracle 19c

Autor: Sistema Java
Fecha: 2026

⚠️ Observaciones Técnicas
Solo se muestra texto plano (no estilos avanzados)
Para renderizar formato real (negritas, tablas, etc.):
Convertir a HTML (POI + XHTML)
Usar JEditorPane con contenido HTML
El uso de fis.available() no es óptimo para archivos grandes
En producción usar streaming robusto.

🚀 Mejora Opcional
Posibles evoluciones del sistema:

Vista tipo Word (renderizado HTML)
Descargar documentos desde Oracle
CRUD completo de documentos
Soporte para PDF + Word
Migración a interfaz moderna con JavaFX : . / .
