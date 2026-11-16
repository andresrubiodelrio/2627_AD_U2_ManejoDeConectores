package Mysql.poolconexionesdbcp;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

public class MainDBCP {

    public static void main(String[] args) throws SQLException {

        BasicDataSource dataSource = null;

        try {
            dataSource = new BasicDataSource();

            dataSource.setUrl(System.getenv().getOrDefault(
                    "DB_URL", "jdbc:mysql://54.90.113.231:3306/empresa"));
            dataSource.setUsername(System.getenv().getOrDefault(
                    "DB_USER", "root"));
            dataSource.setPassword(System.getenv().getOrDefault(
                    "DB_PASSWORD", "root"));

            dataSource.setInitialSize(0);
            dataSource.setMaxTotal(2);
            dataSource.setMaxIdle(2);
            dataSource.setMinIdle(0);
            dataSource.setMaxWait(Duration.ofSeconds(3));
            dataSource.setTestOnBorrow(true);

            // El pool se inicializa al solicitar la primera conexión.
            probarPool(dataSource);

        } finally {
            // Se ejecuta aunque probarPool lance una excepción.
            // Sus conexiones se han devuelto antes, en sus propios finally.
            if (dataSource != null) {
                System.out.println("Cerrando el pool...");
                dataSource.close();
                System.out.println("Pool cerrado: " + dataSource.isClosed());
            }
        }
    }

    private static void probarPool(BasicDataSource dataSource)
            throws SQLException {

        Connection c1 = null;

        try {
            c1 = dataSource.getConnection();
            mostrarEstado(dataSource, "Después de obtener c1");

            Connection c2 = null;

            try {
                c2 = dataSource.getConnection();
                mostrarEstado(dataSource, "Después de obtener c2");

            } finally {
                if (c2 != null) {
                    c2.close();
                }
            }

            mostrarEstado(dataSource, "Después de devolver c2");

        } finally {
            if (c1 != null) {
                c1.close();
            }
        }

        mostrarEstado(dataSource, "Después de devolver c1");
    }

    private static void mostrarEstado(
            BasicDataSource dataSource, String momento) {

        if (dataSource.getConnectionPool() == null) {
            System.out.println("El pool todavía no está iniciado.");
            return;
        }

        int prestadas = dataSource.getNumActive();
        int libres = dataSource.getNumIdle();
        int total = dataSource.getMaxTotal();
        //int total = prestadas + libres;
        int esperando = dataSource.getConnectionPool().getNumWaiters();

        System.out.printf(
                "%s | prestadas=%d | libres=%d | total=%d | esperando=%d%n",
                momento, prestadas, libres, total, esperando
        );
    }
}
