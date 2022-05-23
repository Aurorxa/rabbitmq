package com.github;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-23 10:47:45
 */
public class Consumer1 {
    /**
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "topic_logs";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "Q1";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明一个临时队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定交换机和队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息，将接收到的消息打印在屏幕上。。。");

        // 消费
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者1 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("消费者1 消费的交换机的名称是 = " + message.getEnvelope().getExchange());
            System.out.println("消费者1 消费的 routing key 是 = " + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag) -> {});


    }
}
