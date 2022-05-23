package com.github;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-20 14:54:02
 */
public class RabbitmqUtils {

    /**
     * 获取 Channel 通道
     *
     * @return
     * @throws Exception
     */
    public static Channel getChannel() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("127.0.0.1");

        Connection connection = connectionFactory.newConnection();

        return connection.createChannel();
    }
}
