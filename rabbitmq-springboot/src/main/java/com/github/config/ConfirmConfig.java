package com.github.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-27 10:49:11
 */
@Configuration
public class ConfirmConfig {

    /**
     * 交换机名称
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";

    /**
     * 死信交换机名称
     */
    public static final String DEAD_EXCHANGE_NAME = "dead.exchange";

    /**
     * 队列的名称
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    /**
     * 死信队列
     */
    public static final String DEAD_QUEUE_NAME = "dead.queue";

    /**
     * routing_key
     */
    public static final String CONFIRM_ROUTING_KEY = "confirm";

    /**
     * dead routing key
     */
    public static final String DEAD_ROUTING_KEY = "dead";

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE_NAME);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME)
                .deadLetterExchange(DEAD_EXCHANGE_NAME)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .build();
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE_NAME)
                .build();
    }

    /**
     * 配置绑定关系
     */
    @Bean
    public Binding confirmBinding() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(CONFIRM_ROUTING_KEY);
    }

    /**
     * 配置绑定关系
     */
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }


}
