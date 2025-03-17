package com.joa.remote.iamservice.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpServletUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpServletUtils.class);

    public static Optional<String> getHeader(HttpServletRequest request, String key) {
        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));

        return Optional.ofNullable(headers.get(key));
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); //X-Forwarded-For
        logger.info("> X-Forwarded-For : " + ip);

        if (ip == null) {
            ip = request.getHeader("X-Real-IP");
            logger.info("> X-Real-IP : " + ip);
        }

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.info("> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.info(">  WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            logger.info("> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            logger.info("> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
            logger.info("> getRemoteAddr : "+ip);
        }
        logger.info("> Result : IP Address : "+ip);

        return ip;
    }
}