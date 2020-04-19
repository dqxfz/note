package site.dqxfz.sso.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import site.dqxfz.sso.constant.AmqpConsts;

/**
 * @author WENG Yang
 * @date 2020年04月14日
 **/
@Configuration
@PropertySource(value = {"classpath:properties/config.properties"}, encoding = "utf-8")
public class AmqpConfig {
    @Value("${rabbit.url}")
    private String rabbitUrl;
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(rabbitUrl);
    }
    @Bean
    public AmqpTemplate sendAmqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(AmqpConsts.SESSION_EXCHANGE_TOPIC_NAME);
        return rabbitTemplate;
    }
}
