package com.netply.botchan.web.rest;

import com.netply.botchan.web.rest.events.EventManager;
import com.netply.botchan.web.rest.events.EventManagerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public EventManager eventManager() {
        return EventManagerImpl.getInstance();
    }
}
