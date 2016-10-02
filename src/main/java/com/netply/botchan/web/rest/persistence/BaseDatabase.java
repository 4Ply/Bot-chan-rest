package com.netply.botchan.web.rest.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDatabase {
    private Connection connection;
    private String mysqlIp;
    private int mysqlPort;
    private String mysqlDb;
    private final String mysqlUser;
    private final String mysqlPassword;


    public BaseDatabase(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        this.mysqlIp = mysqlIp;
        this.mysqlPort = mysqlPort;
        this.mysqlDb = mysqlDb;
        this.mysqlUser = mysqlUser;
        this.mysqlPassword = mysqlPassword;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = createConnection();
    }

    private Connection createConnection() throws SQLException {
        return createConnectionForCredentials(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    private Connection createConnectionForCredentials(String mysqlIp, int mysqlPort, String mysqlDb, String mysqlUser, String mysqlPassword) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + mysqlIp + ":" + mysqlPort + "/" + mysqlDb, mysqlUser, mysqlPassword);
    }

    protected Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
