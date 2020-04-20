package site.dqxfz.portal.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import site.dqxfz.portal.web.websocket.CoordinationHandler;
import site.dqxfz.portal.web.websocket.FileHandler;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年03月22日
 **/
@Configuration
@EnableWebSocket
@ComponentScan("site.dqxfz.portal.web.websocket")
public class WebSocketConfig implements WebSocketConfigurer {
    private final FileHandler fileHandler;
    private final CoordinationHandler coordinationHandler;

    public WebSocketConfig(FileHandler fileHandler, CoordinationHandler coordinationHandler) {
        this.fileHandler = fileHandler;
        this.coordinationHandler = coordinationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(fileHandler, "/file");
        registry.addHandler(coordinationHandler, "/coordination");
    }
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(20 * 1024 * 1024);
        container.setMaxBinaryMessageBufferSize(20 * 1024 * 1024);
        return container;
    }


}