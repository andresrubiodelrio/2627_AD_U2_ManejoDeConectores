package Mysql.databasemetadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import Mysql.utilidades.MySQL;

public class EjemploDatabaseMetaData {

    private static Connection conexion = MySQL.establecerConexion();

    public static void main(String[] args)
    {
        /* objeto de metadatos */
        DatabaseMetaData dbmd;
        try
        {

            //crea objeto de metadatos y obitene información
            dbmd = conexion.getMetaData();

            informacionBaseDatos(dbmd);
            tablasBaseDatos(dbmd,"empresa");
            columnasTablaBaseDatos(dbmd,"empresa","departamentos");
            primaryKeysTablaBaseDatos(dbmd,"empresa","departamentos");
            primaryKeysTablaBaseDatos(dbmd,"empresa","empleados");
            exportedKeysTablaBaseDatos(dbmd,"empresa","departamentos");
            exportedKeysTablaBaseDatos(dbmd,"empresa","empleados");
            importedKeysTablaBaseDatos(dbmd,"empresa","departamentos");
            importedKeysTablaBaseDatos(dbmd,"empresa","empleados");
            procedimientosBaseDatos(dbmd,"empresa");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }


    }

    private static void informacionBaseDatos(DatabaseMetaData dbmd)
    {
        String nombre, driver, url_name, usuario, catalogo, esquema, tabla, tipo;

        try {
            nombre = dbmd.getDatabaseProductName();
            driver = dbmd.getDriverName();
            url_name = dbmd.getURL();
            usuario = dbmd.getUserName();

            /* imprime valores */
            System.out.println("INFORMACIÓN SOBRE LA BASE DE DATOS:");
            System.out.println("===================================");
            System.out.println("Nombre: " + nombre);
            System.out.println("Driver: " + driver);
            System.out.println("URL: " + url_name);
            System.out.println("Usuario: " + usuario + "\n");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void tablasBaseDatos(DatabaseMetaData dbmd,String catalogo)
    {
        /* variables locales */
        String catalogoBd, tabla, tipo;
        ResultSet resultado;

        System.out.println("TABLAS BASE DE DATOS EMPRESA:");
        System.out.println("=============================");

        /* obtiene la informaciónn de las tablas y vistas existentes */
        try {
            resultado = dbmd.getTables(catalogo, null, null, null);

            while (resultado.next())
            {
                catalogoBd = resultado.getString(1);
                //esquema = result.getString(2);
                tabla = resultado.getString(3);
                tipo = resultado.getString(4);

                /* imprime */
                System.out.println(tipo + " - Catálogo: " + catalogoBd + ", Nombre: " + tabla);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void columnasTablaBaseDatos(DatabaseMetaData dbmd,String catalogo, String tabla)
    {
        String nombreCol, tipoCol, tamanoCol, nula;
        ResultSet resultado;

        System.out.println("\n\nCOLUMNAS TABLA " + tabla.toUpperCase() + ":");
        System.out.println("=============================");

        /* obtiene las columnas de la tabla */
        try {
            resultado = dbmd.getColumns(catalogo, null, tabla, null);

            while (resultado.next())
            {
                nombreCol = resultado.getString("COLUMN_NAME");
                tipoCol = resultado.getString("TYPE_NAME");
                tamanoCol = resultado.getString("COLUMN_SIZE");
                nula = resultado.getString("IS_NULLABLE");

                System.out.println(" Columna: " + nombreCol + ", Tipo: "
                        + tipoCol + ", Tamaño: " + tamanoCol + ", ¿Puede ser Nula?: "+ nula);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());

        }
    }

    private static void primaryKeysTablaBaseDatos(DatabaseMetaData dbmd,String catalogo,String tabla)
    {
        String pkDep = "", separador = "";

        ResultSet resultado;
        System.out.println("\n\nPRIMARY KEYS TABLA "+ tabla.toUpperCase() + ":");
        System.out.println("===================================");

        try {
            resultado = dbmd.getPrimaryKeys(catalogo, null, tabla);

            while (resultado.next())
            {
                pkDep += separador + resultado.getString("COLUMN_NAME");
                separador = "+";
            }

            System.out.println("Clave primaria: " + pkDep);

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void exportedKeysTablaBaseDatos(DatabaseMetaData dbmd,String catalogo,String tabla)
    {
        String fkName, pkName, fkTable, pkTable;
        boolean tieneForeignKey=false;
        ResultSet resultado;

        System.out.println("\n\nEXPORTED KEYS TABLA " + tabla.toUpperCase() + ":");
        System.out.println("===================================");

        try {
            resultado = dbmd.getExportedKeys(catalogo, null, tabla);

            while (resultado.next()) {
                tieneForeignKey=true;
                pkName = resultado.getString("PKCOLUMN_NAME");
                pkTable = resultado.getString("PKTABLE_NAME");

                fkName = resultado.getString("FKCOLUMN_NAME");
                fkTable = resultado.getString("FKTABLE_NAME");

                System.out.print("La clave primaria " + pkName + " de la tabla " + pkTable) ;
                System.out.println(" está como clave foránea con nombre " + fkName + " en la tabla " + fkTable);
            }

            if (!tieneForeignKey)
                System.out.println("La clave primaria de la tabla " + tabla + " de la base de datos " + catalogo + " no está como clave foránea en ninguna tabla.");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void importedKeysTablaBaseDatos(DatabaseMetaData dbmd,String catalogo,String tabla)
    {
        String fkName, pkName, fkTable, pkTable;
        boolean tieneForeignKey=false;
        ResultSet resultado;

        System.out.println("\n\nFOREIGN KEYS TABLA " + tabla.toUpperCase() + ":");
        System.out.println("===================================");

        try {
            resultado = dbmd.getImportedKeys(catalogo, null, tabla);

            while (resultado.next()) {
                tieneForeignKey=true;
                pkName = resultado.getString("PKCOLUMN_NAME");
                pkTable = resultado.getString("PKTABLE_NAME");

                fkName = resultado.getString("FKCOLUMN_NAME");
                fkTable = resultado.getString("FKTABLE_NAME");

                System.out.print("La tabla " + fkTable + " tiene como clave foránea al campo " + fkName +".") ;
                System.out.println(" Dicho campo está como clave primaria en la tabla " + pkTable + " y tiene como nombre " + pkName);
            }

            if (!tieneForeignKey)
                System.out.println("La tabla " + tabla + " de la base de datos " + catalogo + " no tiene clave foránea en ninguna tabla.");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void procedimientosBaseDatos(DatabaseMetaData dbmd, String catalogo)
    {
        ResultSet resultado;
        String proc_name, proc_type, tipo;

        System.out.println("\n\nPROCEDIMIENTOS Y FUNCIONES DE LA BASE DE DATOS");
        System.out.println("================================================");

        try {
            resultado = dbmd.getProcedures(catalogo, null, null);

            while (resultado.next())
            {
                proc_name = resultado.getString("PROCEDURE_NAME");
                proc_type = resultado.getString("PROCEDURE_TYPE");
                tipo= proc_type.equals("1") ? "Procedimiento" : "Función";
                System.out.println("Nombre Procedimiento: " + proc_name + " - Tipo: " + tipo);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }


}
