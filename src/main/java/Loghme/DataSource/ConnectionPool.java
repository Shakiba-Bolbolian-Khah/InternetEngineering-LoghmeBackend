package Loghme.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

//    private static BasicDataSource ds = new BasicDataSource();
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/Loghme");
        dataSource.setUser("root");
        dataSource.setPassword("MirHamed2495");

        dataSource.setInitialPoolSize(5);
        dataSource.setMinPoolSize(5);
        dataSource.setAcquireIncrement(5);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(100);
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        // remote db
//        ds.setUrl("jdbc:mysql://localhost:3306/Loghme");
//        ds.setUsername("root");
//        ds.setPassword("MirHamed2495");
//        ds.setMinIdle(5);
//        ds.setMaxIdle(15);
//        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private ConnectionPool() {
    }
}