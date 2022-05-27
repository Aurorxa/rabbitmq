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
    public static final String EXCHANGE_NAME = "confirm.exchange";

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "confirm.queue";

    /**
     * routing_key
     */
    public static final String ROUTING_KEY = "confirm";

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    /**
     * 配置绑定关系
     */
    @Bean
    public Binding confirmBinding() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(ROUTING_KEY);
    }


}
