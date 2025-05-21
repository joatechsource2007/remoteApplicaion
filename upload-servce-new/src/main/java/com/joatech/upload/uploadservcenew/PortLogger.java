package com.joatech.upload.uploadservcenew;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PortLogger implements ApplicationRunner {

    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("\n✅ 서버가 포트 " + port + "에서 실행 중입니다.\n");
    }
}
