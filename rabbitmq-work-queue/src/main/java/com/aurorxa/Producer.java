package com.aurorxa;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.Channel;

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

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, MapUtil.newHashMap());

        for (int i = 0; i < 10; i++) {
            String msg = "你好啊 " + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("消息发送完毕");

    }
}
