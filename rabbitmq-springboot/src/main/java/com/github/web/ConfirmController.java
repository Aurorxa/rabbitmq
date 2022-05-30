package com.github.web;

import com.github.config.ConfirmConfig;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-27 13:37:39
 */
@Slf4j
@Value
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/confirm")
public class ConfirmController {

    RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/sendMsg/{msg}")
    public String sendMsg(@PathVariable String msg) {
        log.info("发送的消息是：{}", msg);

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                MessagePostProcessor messagePostProcessor = message -> {
                    MessageProperties messageProperties = message.getMessageProperties();
                    // 设置消息的优先级
                    messageProperties.setPriority(5);
                    return message;
                };
                // 发送消息
                rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, msg.getBytes(StandardCharsets.UTF_8), messagePostProcessor);
            }
        }
        // 发送消息
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, msg.getBytes(StandardCharsets.UTF_8));


        return "发送消息";
    }

}
