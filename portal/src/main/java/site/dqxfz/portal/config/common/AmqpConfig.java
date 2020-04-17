package site.dqxfz.portal.config.common;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WENG Yang
 * @date 2020年04月14日
 **/
@Configuration
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
//        rabbitTemplate.setExchange("session.direct");
        return rabbitTemplate;
    }
    @Bean
    public DirectExchange sessionDirectExchange2(){
        return new DirectExchange("session.direct2");
    }
    @Bean
    public DirectExchange sessionDirectExchange(){
        return new DirectExchange("session.direct");
    }
    @Bean
    public Queue sessionQueue(){
        return new Queue("session.queue");
    }
    @Bean
    public Queue sessionQueue2(){
        return new Queue("session.queue2");
    }
    @Bean
    public Binding binding(DirectExchange sessionDirectExchange, Queue sessionQueue){
        return BindingBuilder
                .bind(sessionQueue)
                .to(sessionDirectExchange)
                .with("logout");
    }
}
