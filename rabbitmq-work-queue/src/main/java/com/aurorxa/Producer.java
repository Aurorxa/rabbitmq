package com.aurorxa;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-20 15:52:07
 */
public class Producer {
    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, MapUtil.newHashMap());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();

            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("消息发送完毕");

    }
}
