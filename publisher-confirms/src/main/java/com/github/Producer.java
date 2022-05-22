package com.github;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;

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

    /**
     * 发送消息的个数
     */
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        // 单个发布确认 耗时：341
        singleReleaseConfirmed();
        // 批量发布确认


    }

    /**
     * 批量发布确认
     *
     * @throws Exception
     */
    public static void batchReleaseConfirmed() throws Exception {

    }

    /**
     * 单个发布确认
     *
     * @throws Exception
     */
    public static void singleReleaseConfirmed() throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        // 开启发布确认模式
        channel.confirmSelect();

        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, MapUtil.newHashMap());

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = String.valueOf(i);
            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));

            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息发送成功");
            }
        }

        long endTime = System.currentTimeMillis();

        // 耗时：341
        System.out.println("耗时：" + (endTime - startTime));

        System.out.println("消息发送完毕");
    }
}
