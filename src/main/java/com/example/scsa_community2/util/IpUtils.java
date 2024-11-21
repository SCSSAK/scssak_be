package com.example.scsa_community2.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public class IpUtils {

    // 클라이언트 IP 가져오기
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // 프록시 환경에서 클라이언트 IP 확인
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // 기본 클라이언트 IP
        }

        // IPv6 로컬 주소를 IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    // IP가 허용된 범위에 있는지 확인
    public static boolean isIpAllowed(String clientIp, List<String> allowedRanges) {
        long ipAsLong = ipToLong(clientIp);

        for (String range : allowedRanges) {
            String[] parts = range.split("-");
            long startIp = ipToLong(parts[0]);
            long endIp = ipToLong(parts[1]);

            if (ipAsLong >= startIp && ipAsLong <= endIp) {
                return true;
            }
        }
        return false;
    }

    // IP를 숫자로 변환
    public static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24) |
                (Long.parseLong(parts[1]) << 16) |
                (Long.parseLong(parts[2]) << 8) |
                Long.parseLong(parts[3]);
    }
}
