package Mysql.poolconexioneshicari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.sql.Connection;
import java.sql.SQLException;

public class MainHikariCP {

    public static void main(String[] args) throws SQLException {

        HikariDataSource dataSource = null;

        try {
            HikariConfig configuracion = new HikariConfig();

            configuracion.setJdbcUrl(System.getenv().getOrDefault(
                    "DB_URL", "jdbc:mysql://54.90.113.231:3306/empresa"));
            configuracion.setUsername(System.getenv().getOrDefault(
                    "DB_USER", "root"));
            configuracion.setPassword(System.getenv().getOrDefault(
                    "DB_PASSWORD", "root"));

            configuracion.setMaximumPoolSize(2);
            configuracion.setMinimumIdle(0);
            configuracion.setConnectionTimeout(3000);
            configuracion.setPoolName("PoolEmpresa");

            // Este constructor crea e inicia el pool.
            dataSource = new HikariDataSource(configuracion);

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

    private static void probarPool(HikariDataSource dataSource)
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
            HikariDataSource dataSource, String momento) {

        HikariPoolMXBean monitor = dataSource.getHikariPoolMXBean();

        if (monitor == null) {
            System.out.println("El pool todavía no está iniciado.");
            return;
        }

        System.out.printf(
                "%s | prestadas=%d | libres=%d | total=%d | esperando=%d%n",
                momento,
                monitor.getActiveConnections(),
                monitor.getIdleConnections(),
                monitor.getTotalConnections(),
                monitor.getThreadsAwaitingConnection()
        );
    }
}
