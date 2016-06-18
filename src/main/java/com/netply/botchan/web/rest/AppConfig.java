package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.persistence.Database;
import com.netply.web.security.login.LoginDatabase;
import com.netply.web.security.login.LoginHandler;
import com.netply.web.security.login.SessionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Bean
    public LoginHandler loginHandler(LoginDatabase loginDatabase) {
        return new LoginHandler(loginDatabase);
    }

    @Bean
    public LoginDatabase loginDatabase() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        return new Database();
    }

    @Bean
    public SessionHandler sessionHandler(LoginDatabase loginDatabase) {
        return new SessionHandler(loginDatabase);
    }
}
