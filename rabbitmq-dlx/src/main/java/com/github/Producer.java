package com.github;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 09:30:23
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        channel.exchangeDeclare(Consumer1.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        for (int i = 0; i < 100; i++) {
            String msg = "消息" + i;
            // 为什么要此处设置 TTL ，是因为生产者设置 TTL 更加灵活
            String expiration = String.valueOf(10 * 1000);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().expiration(expiration).build();
            channel.basicPublish(Consumer1.NORMAL_EXCHANGE, Consumer1.NORMAL_ROUTING_KEY, basicProperties, msg.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("发送消息完毕");

    }
}
