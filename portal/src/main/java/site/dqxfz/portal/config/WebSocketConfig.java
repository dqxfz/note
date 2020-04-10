package site.dqxfz.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import site.dqxfz.portal.controller.WebsocketFileHandler;
import site.dqxfz.portal.service.FileService;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年03月22日
 **/
@Configuration
@EnableWebSocket
@ComponentScan("site.dqxfz.portal.controller")
public class WebSocketConfig implements WebSocketConfigurer{
    final WebsocketFileHandler websocketFileHandler;

    public WebSocketConfig(WebsocketFileHandler websocketFileHandler) {
        this.websocketFileHandler = websocketFileHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(websocketFileHandler, "/note");
    }
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(20 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(20 * 1024 * 1024);
        return container;
    }
}