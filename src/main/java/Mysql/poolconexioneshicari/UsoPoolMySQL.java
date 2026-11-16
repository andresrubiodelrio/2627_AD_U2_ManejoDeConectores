package Mysql.poolconexioneshicari;

import java.sql.*;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

public class UsoPoolMySQL {

	public static void main(String[] args) {

		for (int i = 0; i < 15; i++) {
			System.out.println("-------------------------");
			System.out.println("* Llamada : " + i);
			pruebapool(i);
		}

	}

	public static void verestadodelPool() {
		HikariDataSource dataSource = PoolMySQL.getDataSource();
		HikariPoolMXBean poolStats = dataSource.getHikariPoolMXBean();
		System.out.println("Conexiones totales en el pool: " + poolStats.getTotalConnections());
		System.out.println("Conexiones activas en el pool: " + poolStats.getActiveConnections());
		System.out.println("Conexiones inactivas en el pool: " + poolStats.getIdleConnections());
		System.out.println("Conexiones en espera: " + poolStats.getThreadsAwaitingConnection());
	}

	public static void pruebapool(int i) {

			Connection conexion = null;
			try {
				conexion = PoolMySQL.getConnection();
				if (conexion != null) {
					System.out.println("------------------");
					verestadodelPool();

					String sql = "SELECT apellido, salario FROM empleados ";

					PreparedStatement sentencia = conexion.prepareStatement(sql);
					ResultSet rs = sentencia.executeQuery();

					System.out.printf("%15s %15s%n", "APELLIDO", "SALARIO");
					System.out.printf("%15s %15s%n", "---------------", "---------------");

					while (rs.next())
						System.out.printf("%15s %15s%n", rs.getString("apellido"), rs.getFloat("salario"));

					rs.close();
					sentencia.close();

					// al cerrar la conexión la liberamos del pool
					// Si no se cierra llegará un momento en que no pueda asignar más.
					// conexion.close();

					// Liberamos para esos valores de i y vemos las inactivas y activas
					if (i == 1 || i == 2 || i == 3)
						conexion.close();

					System.out.println("------------------");
					verestadodelPool();
				}
			} catch (SQLTransientConnectionException ex) {
				System.out.println("CONEXIÓN NO REALIZADA: ");
				verestadodelPool();
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
}
