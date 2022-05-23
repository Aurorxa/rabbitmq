package com.github;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-23 15:29:42
 */
public class Consumer2 {

    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "direct_logs";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "console";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitmqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 绑定交换机和队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warning");

        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者2 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("消费者2 消费的交换机的名称是 = " + message.getEnvelope().getExchange());
            System.out.println("消费者2 消费的 routing key 是 = " + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag) -> {});


    }
}
