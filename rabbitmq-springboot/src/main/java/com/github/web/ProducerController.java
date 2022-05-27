package com.github.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 生产者
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 14:14:52
 */
@Slf4j
@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{msg}/{ttl}")
    public String msg(@PathVariable("msg") String msg, @PathVariable("ttl") Integer ttl) {
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列:{}", LocalDateTime.now(), ttl, msg);
        MessagePostProcessor messagePostProcessor = (message) -> {
            // 注意，这里不再是 setExpiration ，而是 setDelay
            message.getMessageProperties().setDelay(ttl);
            return message;
        };
        // rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, RabbitmqConfig.ROUTING_KEY, msg, messagePostProcessor);
        return "发送消息成功";
    }

}
