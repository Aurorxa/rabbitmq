package com.github;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
        // singleReleaseConfirmed();
        // 批量发布确认 耗时：39
        // batchReleaseConfirmed();
        // 异步发布确认 耗时：33
        asynchronousReleaseConfirmed();
    }

    /**
     * 异步发布确认
     */
    public static void asynchronousReleaseConfirmed() throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        // 开启发布确认模式
        channel.confirmSelect();

        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, MapUtil.newHashMap());

        /**
         * 线程安全有序的哈希表，适用于高并发的情况
         * ① 可以将序号和消息进行关联。
         * ② 可以批量删除条目。
         * ③ 支持高并发。
         */
        ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();

        // 准备消息的监听器，用来监听那些消息成功了，那些消息失败了。
        // 消息确认成功的回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息的 ID = " + deliveryTag);
            // 删除掉已经确认的消息，剩下的就是未确认的消息
            if (multiple) { // 如果是批量
                // 删除已经确认的消息
                ConcurrentNavigableMap<Long, String> headMap = map.headMap(deliveryTag);
                headMap.clear();
            } else {
                // 只清除当前序列号的消息
                map.remove(deliveryTag);
            }
        };
        // 消息确认失败的回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息的 ID = " + deliveryTag);
            // 输出未确认的消息
            String msg = map.get(deliveryTag);
            System.out.println("未确认的消息 = " + msg);

        };
        // 异步
        channel.addConfirmListener(ackCallback, nackCallback);

        long startTime = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = String.valueOf(i);

            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));

            // 记录所有要发送的消息
            // channel.getNextPublishSeqNo()获取下一个消息的序列号
            map.put(channel.getNextPublishSeqNo(), msg);

        }

        long endTime = System.currentTimeMillis();

        // 耗时：341
        System.out.println("耗时：" + (endTime - startTime));

        System.out.println("消息发送完毕");
    }


    /**
     * 批量发布确认
     *
     * @throws Exception
     */
    public static void batchReleaseConfirmed() throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        // 开启发布确认模式
        channel.confirmSelect();

        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, MapUtil.newHashMap());

        long startTime = System.currentTimeMillis();

        // 批量确认消息的大小
        int batchSize = 100;

        // 批量发送消息，批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String msg = String.valueOf(i);
            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes(StandardCharsets.UTF_8));

            if (i % batchSize == 0) {
                boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("消息发送成功");
                }
            }

        }

        long endTime = System.currentTimeMillis();

        // 耗时：341
        System.out.println("耗时：" + (endTime - startTime));

        System.out.println("消息发送完毕");
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

        // 批量发送消息，单个发布确认
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
