package site.dqxfz.sso.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        return admin;
    }
    @Bean
    public AmqpTemplate sendAmqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(AmqpConsts.SESSION_EXCHANGE_TOPIC_NAME);
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange sessionExchangeTopic(){
        return new DirectExchange(AmqpConsts.SESSION_EXCHANGE_TOPIC_NAME);
    }
    @Bean
    public Queue sessionQueue(){
        return new Queue(AmqpConsts.SESSION_QUEUE_NAME);
    }
    @Bean
    public Binding binding(DirectExchange sessionExchangeTopic, Queue sessionQueue){
        return BindingBuilder
                .bind(sessionQueue)
                .to(sessionExchangeTopic)
                .with(AmqpConsts.SESSION_ROUTING_KEY_NAME);
    }
}
