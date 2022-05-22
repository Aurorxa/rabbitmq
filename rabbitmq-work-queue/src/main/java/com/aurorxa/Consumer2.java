package com.aurorxa;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * 消费者2
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-20 15:53:45
 */
public class Consumer2 {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitmqUtils.getChannel();

        // 消费者设置不公平分发
        int prefetchCount = 300;
        channel.basicQos(prefetchCount);

        channel.queueDeclare(QUEUE_NAME, true, false, false, MapUtil.newHashMap());

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                // 睡眠 10 s
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("消费者2 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));

            // 手动应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);

        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("consumerTag = " + consumerTag);
        };

        // 设置为手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
