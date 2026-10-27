package Mysql.dml;

import Mysql.utilidades.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploConstantesResultset {

    private static Connection conexion = MySQL.establecerConexion();

    public static void main(String[] args) {
        Statement sentencia = null;
        try {
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = sentencia.executeQuery("SELECT emp_no, apellido, salario FROM empleados");

            while (rs.next()) {
                float salario = rs.getFloat("salario");

                if (salario<1000)
                    rs.updateFloat("salario", salario + 200); // Se suma 100
                else if (salario>=1000 && salario<=2000)
                    rs.updateFloat("salario", salario + 100);
                else
                    rs.updateFloat("salario", salario+50);

                rs.updateRow(); // Se actualiza

                System.out.println("Actualizado: " + rs.getString("apellido") + ", salario anterior: " + salario
                        + ", nuevo: " + rs.getFloat("salario"));
            }

            rs.close();
            sentencia.close();
            MySQL.cerrarConexion();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}
