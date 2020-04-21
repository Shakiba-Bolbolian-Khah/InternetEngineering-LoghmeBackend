package Loghme.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        // remote db
        ds.setUrl("jdbc:mysql://localhost:3306/Loghme");
        ds.setUsername("root");
        ds.setPassword("MirHamed2495");
        ds.setMinIdle(5);
        ds.setMaxIdle(15);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private ConnectionPool() {
    }
}