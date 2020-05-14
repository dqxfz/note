package site.dqxfz.portal.config.common;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import site.dqxfz.portal.constant.AmqpConsts;
import site.dqxfz.portal.web.amqp.UserMessageListener;

/**
 * @author WENG Yang
 * @date 2020年04月14日
 **/
@Configuration
@ComponentScan({"site.dqxfz.portal.web.amqp"})
public class AmqpConfig {
    @Value("${rabbit.url}")
    private String rabbitUrl;
    private final UserMessageListener userMessageListener;

    public AmqpConfig(UserMessageListener userMessageListener) {
        this.userMessageListener = userMessageListener;
    }

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
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(AmqpConsts.SESSION_QUEUE_NAME);

        MessageListenerAdapter adapter = new MessageListenerAdapter(userMessageListener);
        //设置处理器的消费消息的默认方法
        adapter.setDefaultListenerMethod("onMessage");
        container.setMessageListener(adapter);
        container.setMessageListener(adapter);
        return container;
    }
//    @Bean
//    public TopicExchange sessionExchangeTopic(){
//        return new TopicExchange(AmqpConsts.SESSION_EXCHANGE_TOPIC_NAME);
//    }
//    @Bean
//    public Queue sessionQueue(){
//        return new Queue(AmqpConsts.SESSION_QUEUE_NAME);
//    }
//    @Bean
//    public Binding binding(TopicExchange sessionExchangeTopic, Queue sessionQueue){
//        return BindingBuilder
//                .bind(sessionQueue)
//                .to(sessionExchangeTopic)
//                .with(AmqpConsts.SESSION_ROUTING_KEY_NAME);
//    }
}
