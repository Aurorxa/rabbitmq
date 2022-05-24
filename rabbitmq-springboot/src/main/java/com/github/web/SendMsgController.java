package com.github.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-24 15:29:40
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        log.info("当前时间：{},发送一条消息给两个 TTL 队列：{}", LocalDateTime.now(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 TTL 为 10 秒的队列：" + msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 TTL 为 40 秒的队列：" + msg);
        return "发送" + msg + "成功";
    }

}
