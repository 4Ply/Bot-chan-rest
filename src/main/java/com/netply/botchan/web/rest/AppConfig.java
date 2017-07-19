package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.events.EventManager;
import com.netply.botchan.web.rest.events.EventManagerImpl;
import com.netply.botchan.web.rest.games.TrackedPlayerDatabase;
import com.netply.botchan.web.rest.games.TrackedPlayerDatabaseImpl;
import com.netply.botchan.web.rest.games.TrackedPlayerManager;
import com.netply.botchan.web.rest.games.TrackedPlayerManagerImpl;
import com.netply.botchan.web.rest.messaging.MessageDatabase;
import com.netply.botchan.web.rest.messaging.MessageDatabaseImpl;
import com.netply.botchan.web.rest.messaging.MessageManager;
import com.netply.botchan.web.rest.messaging.MessageManagerImpl;
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
        return new LoginDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public SessionHandler sessionHandler(LoginDatabase loginDatabase) {
        return new SessionHandler(loginDatabase);
    }

    @Bean
    public MessageDatabase messageDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new MessageDatabaseImpl(mysqlIp, mysqlPort, mysqlDb, mysqlUser, mysqlPassword);
    }

    @Bean
    public MessageManager messageManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new MessageManagerImpl(messageDatabase());
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
