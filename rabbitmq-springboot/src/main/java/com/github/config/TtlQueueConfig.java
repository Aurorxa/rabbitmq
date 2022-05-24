package com.github.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置类，用来绑定关系。
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-24 14:15:38
 */
@Configuration
public class TtlQueueConfig {

    /**
     * 普通交换机的名称
     */
    public static final String EXCHANGE_X = "X";

    /**
     * 死信交换机的名称
     */
    public static final String EXCHANGE_DEAD_Y = "Y";

    /**
     * 普通队列的名称 QA
     */
    public static final String QUEUE_A = "QA";

    /**
     * 普通队列的名称 QB
     */
    public static final String QUEUE_B = "QB";

    /**
     * 死信队列的名称 QD
     */
    public static final String QUEUE_DEAD_D = "QD";

    /**
     * 配置交换机
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(EXCHANGE_X, true, false, null);
    }

    /**
     * 配置死信交换机
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(EXCHANGE_DEAD_Y, true, false, null);
    }

    /**
     * 配置队列 TTL 10s
     */
    @Bean("aQueue")
    public Queue aQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置 TTL 时间
        arguments.put("x-message-ttl", 10 * 1000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_Y);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    /**
     * 配置队列 TTL 40s
     */
    @Bean("bQueue")
    public Queue bQueue() {
        Map<String, Object> arguments = new HashMap<>(3);
        // 设置 TTL 时间
        arguments.put("x-message-ttl", 40 * 1000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", EXCHANGE_DEAD_Y);
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    /**
     * 配置队列
     */
    @Bean("dQueue")
    public Queue dQueue() {
        return QueueBuilder.durable(QUEUE_DEAD_D).build();
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queueABindingX(@Qualifier("aQueue") Queue aQueue,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(aQueue).to(xExchange).with("XA");
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queueBBindingX(@Qualifier("bQueue") Queue bQueue,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(bQueue).to(xExchange).with("XB");
    }

    /**
     * 绑定
     */
    @Bean
    public Binding queueDBindingY(@Qualifier("dQueue") Queue dQueue,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(dQueue).to(yExchange).with("YD");
    }


}
