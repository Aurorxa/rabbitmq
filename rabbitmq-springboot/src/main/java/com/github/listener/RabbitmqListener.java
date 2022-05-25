package com.github.listener;

import com.github.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 14:20:14
 */
@Slf4j
@Component
public class RabbitmqListener {

    @RabbitListener(queues = RabbitmqConfig.QUEUE)
    public void receive(Message message) {
        log.info("当前时间：{},收到延时队列的消息：{}", LocalDateTime.now(), new String(message.getBody(), StandardCharsets.UTF_8));
    }

}
