package com.aurorxa;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-19 14:57:34
 */
public class Consumer {
    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置连接 RabbitMQ 的信息
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        // 声明消费者成功消费的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("consumerTag = " + consumerTag);
            System.out.println("message = " + new String(message.getBody(), StandardCharsets.UTF_8));

        };

        // 声明消费者取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("consumerTag = " + consumerTag);
        };


        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

        // 关闭信道
        channel.close();
        // 关闭连接
        connection.close();
        // 关闭连接工厂
        connectionFactory.clone();

    }
}
