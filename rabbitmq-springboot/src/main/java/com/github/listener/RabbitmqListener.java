package com.github.listener;

import com.github.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 14:20:14
 */
@Slf4j
@Component
public class RabbitmqListener {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receive(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("接收到的消息是：{}", msg);
    }

}
