package Mysql.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostGreSQL {

    private static final String HOST = "localhost:5432";
    private static final String BBDD = "empresa";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "ad2627";

    private static Connection conexion = null;

    private PostGreSQL() {
        //Evitamos que se creen instancias
    }

    public static Connection establecerConexion()
    {
        if (conexion != null) {
            return conexion;
        }

        try {
            conexion = DriverManager.getConnection("jdbc:postgresql://"+HOST+"/"+BBDD,USUARIO,CONTRASENA);
            System.out.println("Conexión a Postgresql realizada correctamente.");
        } catch (SQLException e) {
            System.out.println("ERROR Postgresql:  "+ e.toString());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a Postgresql cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("ERROR Postgresql: "+ e.toString());
        }
    }

}