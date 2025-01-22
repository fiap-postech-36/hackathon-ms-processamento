package br.com.videoprocessing.application.infra;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "video-processing-queue";
    public static final String EXCHANGE_NAME = "video-processing-exchange";
    public static final String KEY_NAME = "video.processing.routing.key";
    public static final String DLQ_QUEUE_NAME = "video-processing-dlx-queue";
    public static final String DLX_EXCHANGE_NAME = "video-processing-dlx-exchange";
    public static final String DLX_KEY_NAME = "video.processing.dlx.routing.key";
    public static final String EMAIL_QUEUE_NAME = "email-queue";
    public static final String EMAIL_EXCHANGE_NAME = "email-exchange";
    public static final String EMAIL_KEY_NAME = "email.routing.key";

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).deadLetterExchange(DLX_EXCHANGE_NAME).deadLetterRoutingKey(DLX_KEY_NAME).build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(KEY_NAME);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DLX_KEY_NAME);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(EMAIL_KEY_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
