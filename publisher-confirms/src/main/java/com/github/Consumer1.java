package com.github;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

/**
 * 消费者1
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-20 15:53:45
 */
public class Consumer1 {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        // 消费者设置不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        channel.queueDeclare(QUEUE_NAME, true, false, false, MapUtil.newHashMap());

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                // 睡眠 1 s
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("消费者1 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));

            // 第一个参数：消息的标记
            // 第二个参数：不批量
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("consumerTag = " + consumerTag);
        };

        // 设置为手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
