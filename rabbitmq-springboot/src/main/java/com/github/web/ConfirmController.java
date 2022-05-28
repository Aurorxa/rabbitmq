package com.github.web;

import com.github.config.ConfirmConfig;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
        String id = "1";
        CorrelationData correlationData = new CorrelationData(id);
        // 发送消息
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, msg.getBytes(StandardCharsets.UTF_8), correlationData);

        // 注意：消息2 的路由不可达
        id = "2";
        correlationData = new CorrelationData(id);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY + id, msg.getBytes(StandardCharsets.UTF_8), correlationData);

        return "发送消息";
    }

}
