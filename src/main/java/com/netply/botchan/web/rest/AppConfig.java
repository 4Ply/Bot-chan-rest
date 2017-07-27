package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.events.EventManager;
import com.netply.botchan.web.rest.events.EventManagerImpl;
import com.netply.botchan.web.rest.persistence.LoginDatabaseImpl;
import com.netply.web.security.login.LoginDatabase;
import com.netply.web.security.login.LoginHandler;
import com.netply.web.security.login.SessionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Value("${mysql_ip}")
    private String mysqlIp;

    @Value("${mysql_port}")
    private int mysqlPort;

    @Value("${mysql_db}")
    private String mysqlDb;

    @Value("${mysql_user}")
    private String mysqlUser;

    @Value("${mysql_password}")
    private String mysqlPassword;

    @Value("${spring.datasource.url}")
    private String datasourceURL;


    @Bean
    public LoginHandler loginHandler(LoginDatabase loginDatabase) {
        return new LoginHandler(loginDatabase);
    }

    @Bean
    public LoginDatabase loginDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new LoginDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public SessionHandler sessionHandler(LoginDatabase loginDatabase) {
        return new SessionHandler(loginDatabase);
    }

    @Bean
    public EventManager eventManager() {
        return EventManagerImpl.getInstance();
    }
}
