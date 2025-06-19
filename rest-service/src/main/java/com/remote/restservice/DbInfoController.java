package com.remote.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DbInfoController {

    @Autowired
    private Environment env;

    @GetMapping("/db-info")
    public Map<String, String> getDatabaseInfo() {
        String fullUrl = env.getProperty("spring.datasource.url", "unknown");

        // DB 이름 추출
        String dbName = fullUrl.replaceAll(".*databaseName=([^;]+).*", "$1");

        // user, password 제거
        String maskedUrl = fullUrl.replaceAll("(?i);user=[^;]*", "")
                .replaceAll("(?i);password=[^;]*", "");

        Map<String, String> result = new HashMap<>();
        result.put("dbUrl", maskedUrl);
        result.put("databaseName", dbName);
        return result;
    }
}
