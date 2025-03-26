package com.edugo.edugo_tcc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Configuração para a exchange "alunos.exchange" (monolito -> microsserviço)
    @Bean
    public FanoutExchange alunosExchange() {
        return new FanoutExchange("alunos.exchange", true, false);
    }

    // Configuração para a exchange "alunos.reverse.exchange" (microsserviço -> monolito)
    @Bean
    public FanoutExchange alunosReverseExchange() {
        return new FanoutExchange("alunos.reverse.exchange", true, false);
    }

    // Queue para o monolito receber eventos do microsserviço
    @Value("${rabbitmq.alunos.reverse.queue}")
    private String alunosReverseQueueName;

    @Bean
    public Queue alunosReverseQueue() {
        return new Queue(alunosReverseQueueName, true); // Durable queue
    }

    // Binding da queue do monolito para a exchange "alunos.reverse.exchange"
    @Bean
    public Binding alunosReverseBinding(Queue alunosReverseQueue, FanoutExchange alunosReverseExchange) {
        return BindingBuilder.bind(alunosReverseQueue).to(alunosReverseExchange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}