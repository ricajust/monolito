package com.edugo.edugo_tcc.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange alunoExchange() {
        return new FanoutExchange("aluno-exchange");
    }
}
