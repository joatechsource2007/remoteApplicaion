package com.joa.remote.iamservice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableDiscoveryClient
public class IamServiceApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IamServiceApplication.class);
        springApplication.setBannerMode(Banner.Mode.LOG);
        springApplication.setLogStartupInfo(true);
        springApplication.run(args);
    }

    @PostConstruct
    public void logPortInfo() {
        String port = env.getProperty("server.port", "8080");
        System.out.println("\n\n");
        System.out.println("===================================================");
        System.out.println("🚀 IAM SERVICE RUNNING ON PORT: " + port);
        System.out.println("===================================================");
        System.out.println("\n\n");
    }

    @Bean  // 앱 구동 후 바로 동작
    public ApplicationListener<ContextRefreshedEvent> startupListener() {
        return event -> {
            System.out.println(" - 서비스 구동 시작 : " +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            System.out.println(" - Project build number : 0.0.1");
        };
    }

}
