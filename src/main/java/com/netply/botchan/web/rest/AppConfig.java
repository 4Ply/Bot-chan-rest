package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.persistence.Database;
import com.netply.web.security.login.LoginDatabase;
import com.netply.web.security.login.LoginHandler;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Value("${key.database.mysql.ip}")
    private String mysqlIp;

    @Value("${key.database.mysql.port}")
    private int mysqlPort;

    @Value("${key.database.mysql.db}")
    private String mysqlDb;

    @Value("${key.database.mysql.user}")
    private String mysqlUser;

    @Value("${key.database.mysql.password}")
    private String mysqlPassword;


    @Bean
    public LoginHandler loginHandler(LoginDatabase loginDatabase) {
        return new LoginHandler(loginDatabase);
    }

    @Bean
    public LoginDatabase loginDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new Database(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public SessionHandler sessionHandler(LoginDatabase loginDatabase) {
        return new SessionHandler(loginDatabase);
    }
}
