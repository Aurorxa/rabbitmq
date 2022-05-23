package com.github;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-23 15:38:35
 */
public class Producer {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        Map<String, String> map = new HashMap<>();
        map.put("quick.orange.rabbit", "Q1 和 Q2 能接收到");
        map.put("lazy.orange.elephant", "Q1 和 Q2 能接收到");
        map.put("quick.orange.fox", "Q1 能接收到");
        map.put("lazy.brown.fox", "Q2 能接收到");
        map.put("lazy.pink.rabbit", "Q2 能接收到");
        map.put("quick.brown.fox", "不匹配任何绑定，不会被任何队列接收到，会被丢弃");
        map.put("quick.orange.male.rabbit", "是四个单词，不匹配任何绑定，会被丢弃");
        map.put("lazy.orange.male.rabbit", "是四个单词，但匹配 Q2");

        map.forEach((k, v) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, k, null, v.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
