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

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        Map<String, String> map = new HashMap<>();
        map.put("info", "info 级别的日志信息");
        map.put("warning", "warning 级别的日志信息");
        map.put("error", "error 级别的日志信息");
        map.put("debug", "debug 级别的日志信息");

        map.forEach((k, v) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, k, null, v.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
