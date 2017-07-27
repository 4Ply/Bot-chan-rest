package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.events.EventManager;
import com.netply.botchan.web.rest.events.EventManagerImpl;
import com.netply.botchan.web.rest.games.TrackedPlayerDatabase;
import com.netply.botchan.web.rest.games.TrackedPlayerDatabaseImpl;
import com.netply.botchan.web.rest.games.TrackedPlayerManager;
import com.netply.botchan.web.rest.games.TrackedPlayerManagerImpl;
import com.netply.botchan.web.rest.permissions.PermissionDatabase;
import com.netply.botchan.web.rest.permissions.PermissionDatabaseImpl;
import com.netply.botchan.web.rest.permissions.PermissionManager;
import com.netply.botchan.web.rest.permissions.PermissionManagerImpl;
import com.netply.botchan.web.rest.persistence.LoginDatabaseImpl;
import com.netply.botchan.web.rest.user.UserDatabase;
import com.netply.botchan.web.rest.user.UserDatabaseImpl;
import com.netply.botchan.web.rest.user.UserManager;
import com.netply.botchan.web.rest.user.UserManagerImpl;
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

    @Bean
    public TrackedPlayerDatabase trackedPlayerDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new TrackedPlayerDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public TrackedPlayerManager trackedPlayerManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new TrackedPlayerManagerImpl(trackedPlayerDatabase());
    }

    @Bean
    public PermissionDatabase permissionDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new PermissionDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public PermissionManager permissionManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new PermissionManagerImpl(permissionDatabase(), userManager());
    }

    @Bean
    public UserDatabase userDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new UserDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public UserManager userManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new UserManagerImpl(userDatabase());
    }
}
