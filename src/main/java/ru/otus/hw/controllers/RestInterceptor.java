package ru.otus.hw.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
public class RestInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String colorMethod = switch (request.getMethod()) {
            case "GET" -> ANSIColors.BLUE;
            case "PUT" -> ANSIColors.YELLOW;
            case "DELETE" -> ANSIColors.RED;
            case "PATCH" -> ANSIColors.CYAN;
            case "POST" -> ANSIColors.GREEN;
            default -> ANSIColors.RESET;
        };
        StringBuilder message = new StringBuilder();
        message.append("API Request  ")
                .append("[").append(request.getProtocol()).append("] ")
                .append(colorMethod).append(request.getMethod()).append(ANSIColors.RESET)
                .append(" ").append(request.getRequestURI());
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr!=null && !ipAddr.equals("")) {
            message.append(" (").append(ipAddr).append(")");
        }
        log.info(message.toString());
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String colorMethod = switch (request.getMethod()) {
            case "GET" -> ANSIColors.BLUE;
            case "PUT" -> ANSIColors.YELLOW;
            case "DELETE" -> ANSIColors.RED;
            case "PATCH" -> ANSIColors.CYAN;
            case "POST" -> ANSIColors.GREEN;
            default -> ANSIColors.RESET;
        };
        StringBuilder message = new StringBuilder();
        message.append("API Response ")
                .append("[").append(request.getProtocol()).append("] ")
                .append(colorMethod).append(request.getMethod()).append(ANSIColors.RESET)
                .append(" ").append(request.getRequestURI())
                .append(" - ").append(response.getStatus()).append(" ").append(HttpStatus.valueOf(response.getStatus()).getReasonPhrase());
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr!=null && !ipAddr.equals("")) {
            message.append(" (").append(ipAddr).append(")");
        }
        log.info(message.toString());
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

}
