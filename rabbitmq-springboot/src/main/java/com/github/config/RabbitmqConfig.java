package com.github.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类，用来声明交换机和队列，并配置之间的关系
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 13:53:15
 */
@Configuration
public class RabbitmqConfig {

    /**
     * 普通交换机
     */
    public static final String EXCHANGE = "delayed.exchange";

    /**
     * routingkey
     */
    public static final String ROUTING_KEY = "delayed.routingkey";

    /**
     * 普通队列
     */
    public static final String QUEUE = "delayed.queue";

    @Bean
    public CustomExchange exchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 声明队列
     */
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    /**
     * 绑定关系
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY).noargs();
    }


}
