package com.github;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-24 10:20:12
 */
public class Producer {

    /**
     * 普通交换机的名称
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    /**
     * routing key
     */
    public static final String NORMAL_ROUTING_KEY = "normal";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        System.out.println("生产者发送消息");
        // 发送消息
        for (int i = 0; i < 10; i++) {
            String msg = "消息" + i;
            // 设置 TTL 时间
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().expiration("100000").build();
            channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, basicProperties, msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
