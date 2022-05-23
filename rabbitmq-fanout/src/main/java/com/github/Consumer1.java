package com.github;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
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
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定交换机和队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，将接收到的消息打印在屏幕上。。。");


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者1 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("consumerTag = " + consumerTag);
        };
        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);


    }
}
