package com.aurorxa;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-19 14:57:05
 */
public class Producer {

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
        // 声明创建队列
        /**
         * 第一个参数：队列的名称
         * 第二个参数：是否持久化【存储在磁盘上】，默认为 false ，表示存储在内存中。
         * 第三个参数：
         *      当 exclusive = true 则设置队列为排他的。如果一个队列被声明为排他队列，该队列 仅对首次声明它的连接（Connection）可见，是该Connection私有的，类似于加锁，并在连接断开connection.close()时自动删除 ；
         *      当 exclusive = false 则设置队列为非排他的，此时不同连接（Connection）的管道Channel可以使用该队列 ；
         * 第四个参数：是否自动删除。如果autoDelete = true，当所有消费者都与这个队列断开连接时，这个队列会自动删除。注意： 不是说该队列没有消费者连接时该队列就会自动删除，因为当生产者声明了该队列且没有消费者连接消费时，该队列是不会自动删除的。
         * 第五个参数：其他参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, MapUtil.newHashMap());
        // 发送消息
        String msg = "你好啊";

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));

        System.out.println("消息发送完毕");

        // 关闭信道
        channel.close();
        // 关闭连接
        connection.close();
        // 关闭连接工厂
        connectionFactory.clone();


    }
}
