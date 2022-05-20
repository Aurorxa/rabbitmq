package com.aurorxa;

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

        channel.queueDeclare(QUEUE_NAME, true, false, false, MapUtil.newHashMap());

        // 声明消费者成功消费的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("consumerTag = " + consumerTag);
            System.out.println("消费者1 消费的 message = " + new String(message.getBody(), StandardCharsets.UTF_8));

            try {
                // 睡眠 1 s
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 第一个参数：消息的标记
            // 第二个参数：不批量
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };

        // 声明消费者取消消费的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("consumerTag = " + consumerTag);
        };

        // 设置为手动应答
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
