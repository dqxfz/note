package site.dqxfz.portal.config.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
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
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware {
    private ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ac = ac;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebsocketFileHandler websocketFileHandler = ac.getBean(WebsocketFileHandler.class);
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