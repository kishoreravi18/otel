package com.yubi.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private static final String USER_AGENT_HEADER = "User-Agent";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("{ \"requestMethod\": \"{}\", \"protocol\": \"{}\", \"requestUrl\": \"{}\", \"requestSize\": \"{}\", \"userAgent\": \"{}\", \"remoteIp\": \"{}\"," +
            "\"serverIp\": \"{}\" }", request.getMethod(), request.getProtocol(), request.getRequestURI(),
            request.getHeader(CONTENT_LENGTH_HEADER), request.getHeader(USER_AGENT_HEADER), request.getRemoteAddr(),
            request.getServerName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("{ \"requestMethod\": \"{}\", \"protocol\": \"{}\", \"requestUrl\": \"{}\", \"requestSize\": \"{}\",\"status\": {}, \"responseSize\": \"{}\", \"userAgent\": \"{}\", \"remoteIp\": \"{}\"," +
            "\"serverIp\": \"{}\" }", request.getMethod(), request.getProtocol(), request.getRequestURI(),
            request.getHeader(CONTENT_LENGTH_HEADER), response.getStatus(), response.getHeader(CONTENT_LENGTH_HEADER),  request.getHeader(USER_AGENT_HEADER), request.getRemoteAddr(),
            request.getServerName());
    }
}
