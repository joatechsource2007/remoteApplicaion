package com.remote.restservice;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class})
public class RestServiceApplication {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(RestServiceApplication.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.setLogStartupInfo(true);
        springApplication.run(args);

    }

    @Bean  //앱 구동 후 바로 동작
    public ApplicationListener<ContextRefreshedEvent> startupListener() {
        return event -> {
            System.out.println(" - 서비스 구동 시작 : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            System.out.println(" - Project build number : 0.0.1");
        };
    }
}
