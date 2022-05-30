package com.github.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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
     * 备份交换机名称
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";

    /**
     * 队列的名称
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    /**
     * 备份队列的名称
     */
    public static final String BACKUP_QUEUE_NAME = "backup.queue";

    /**
     * routing_key
     */
    public static final String CONFIRM_ROUTING_KEY = "confirm";

    /**
     * 配置交换机
     */
    @Bean
    public DirectExchange confirmExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange", BACKUP_EXCHANGE_NAME);
        return new DirectExchange(CONFIRM_EXCHANGE_NAME, true, false, arguments);
    }

    /**
     * 配置备份交换机
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    /**
     * 配置队列
     */
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    /**
     * 配置备份队列
     */
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
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
    public Binding backupBinding() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }


}
