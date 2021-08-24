package com.example.onedaypiece.chat.config;

import com.example.onedaypiece.chat.config.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //sub용 sub topic/public -> /sub
        config.enableSimpleBroker("/sub");
        //메시지 보낼 url send /app/message ->/pub
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint : client 연결
        // URL//chatting  <-웹소켓 연결 주소
        registry.addEndpoint("/chatting").setAllowedOriginPatterns("*://*")
                .withSockJS();
        log.info("websocket 연결 성공");
    }

    //stomphandler가 token, type 체크 먼저 할 수 있게 interceptor
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
        log.info("stomphandler 가자");
    }
}
