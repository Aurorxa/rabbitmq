package com.github;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-24 10:43:13
 */
public class Consumer2 {
    /**
     * 死信交换机的名称
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";

    /**
     * 死信队列的名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    /**
     * routing key
     */
    public static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitmqUtils.getChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者2 消费的消息 = " + new String(message.getBody(), StandardCharsets.UTF_8));
        };


        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, (consumerTag) -> {});

    }
}
