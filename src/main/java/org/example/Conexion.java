package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class Conexion {

    Connection conectar = null;

    String usuario = "root";
    String contraseña = "root";
    String bd = "tpfinal_correlatividades";
    String ip = "localhost";
    String puerto = "3306";

    String ruta = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection abrirConexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(ruta, usuario, contraseña);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Hubo un error al conectarse" + e);
        }

        return conectar;

    }

    public void cerrarConexion() throws SQLException {
        try {
            conectar.close();
        } catch (Exception e) {
        }
    }
}