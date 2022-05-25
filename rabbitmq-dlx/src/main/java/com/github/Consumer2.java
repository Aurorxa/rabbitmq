package com.github;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 09:27:41
 */
public class Consumer2 {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        /* 其实：下面的声明和绑定也可以不写，因为我们在 Consumer1 中已经配置过了 */
        /* 声明死信交换机  direct 模式 */
        channel.exchangeDeclare(Consumer1.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        /* 声明死信队列 */
        channel.queueDeclare(Consumer1.DEAD_QUEUE, true, false, false, null);

        /* 绑定死信交换机和死信队列 */
        channel.queueBind(Consumer1.DEAD_QUEUE, Consumer1.DEAD_EXCHANGE, Consumer1.DEAD_ROUTING_KEY);


        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者2消费的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(Consumer1.DEAD_QUEUE, true, deliverCallback, (consumerTag) -> {});

    }
}
