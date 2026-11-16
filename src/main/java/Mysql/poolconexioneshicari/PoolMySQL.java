package Mysql.poolconexioneshicari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;

public class PoolMySQL {
	private static HikariDataSource dataSource;

	static {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://54.90.113.231:3306/empresa");
		config.setUsername("root");
		config.setPassword("root");
		config.setMaximumPoolSize(10); // Máximo de conexiones en el pool
		config.setMinimumIdle(2); // Mínimo de conexiones inactivas
		
		config.setIdleTimeout(30000); // Tiempo de espera antes de liberar conexiones ms
		
		config.setConnectionTimeout(30000); // Tiempo máximo de espera para obtener conexión 30 seg

		config.setMaxLifetime(1800000); // Tiempo máximo de vida de una conexión 30 minutos
	
		
		dataSource = new HikariDataSource(config);
			
	}

	public static Connection getConnection() throws SQLTransientConnectionException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
			System.out.println("ERROR: No se puede obtener una nueva conexión. Conexión no disponible. Se han superado los 30 segundos de espera.");
			System.out.println(e.getMessage());
		}
		return null;
    }

	public static HikariDataSource getDataSource() {
	    return dataSource;
	}
	
	public static void closePool() {
		if (dataSource != null) {
			dataSource.close();
		}
	}
}
