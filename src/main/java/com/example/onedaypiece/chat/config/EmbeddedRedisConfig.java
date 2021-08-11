//package com.example.onedaypiece.chat.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.util.StringUtils;
//import redis.embedded.RedisServer;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//
//@Profile("local")
//@Configuration
//public class EmbeddedRedisConfig {
//
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    private RedisServer redisServer;
//
//    @PostConstruct
//    public void redisServer() throws IOException {
//        int port = isRedisRunning() ? findAvailablePort():redisPort ;
//        redisServer = new RedisServer(port);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if (redisServer != null) {
//            redisServer.stop();
//        }
//    }
//
//    /**
//     * Embedded Redis가 현재 실행중인지 확인
//     */
//    private boolean isRedisRunning() throws IOException {
//
//        return isRunning(executeGrepProcessCommand(redisPort));
//    }
//
//    /**
//     * 현재 PC/서버에서 사용가능한 포트 조회
//     */
//    public int findAvailablePort() throws IOException {
//        for (int port = 10000; port <= 65535; port++) {
//            Process process = executeGrepProcessCommand(port);
//            if (!isRunning(process)) {
//                return port;
//            }
//        }
//
//        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
//    }
//
//    /**
//     * 윈도우인지 리눅스인지 확인
//     */
//    private boolean windowsOrLinux(){
//        boolean isWindows = System.getProperty("os.name")
//            .toLowerCase().startsWith("windows");
//        System.out.println("실행환경이 윈도우인가? " + isWindows);
//        return isWindows;
//    }
//
///**
// * 해당 port를 사용중인 프로세스 확인하는 sh 실행
// */
//    private Process executeGrepProcessCommand(int port) throws IOException {
//        if(windowsOrLinux()) {
//            String command = String.format("netstat -an | findstr %d", port);
//            String[] shell = {"cmd", "/c", command};
//            return Runtime.getRuntime().exec(shell);
//        }else{
//            System.out.println("port = " + port);
//            String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
//            String[] shell = {"/bin/sh", "-c", command};
//            return Runtime.getRuntime().exec(shell);
//        }
//    }
//
//    /**
//     * 해당 Process가 현재 실행중인지 확인
//     */
//    private boolean isRunning(Process process) {
//        String line;
//        StringBuilder pidInfo = new StringBuilder();
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//            while ((line = input.readLine()) != null) {
//                pidInfo.append(line);
//            }
//        } catch (Exception e) {
//        }
//        return !StringUtils.isEmpty(pidInfo.toString());
//    }
//}