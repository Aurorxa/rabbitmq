package com.github.listener;

import com.github.config.ConfirmConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 14:20:14
 */
@Slf4j
@Component
public class RabbitmqListener {


    // 重试次数
    private int retryCount = 0;

    /**
     * 如果是重试机制，就不能使用 try-catch 捕获，否则重试机制将失效。
     * 如果是自动 ack ，多次重试还是失败，消息就会自动确认，消息就会丢失；
     * 如果是手动 ack ，多次重试还是失败，也无法 nack ，就会一直处于 unacked 状态，导致消息积压。
     *
     * @param message
     * @param channel
     * @param tag
     * @throws IOException
     */
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receive(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            // 处理主要业务
            log.info("接收到的消息是：{}", msg);
            int i = 10 / 0;
            // 确认消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            retryCount++;
            log.info("重试次数：{}", retryCount);
            // 处理业务失败，还要进行其他操作，比如记录失败原因
            log.info("记录失败原因 ====>");
            // 抛出异常，触发重试机制
            throw e;
        } finally {
            // 重试次数达到限制
            if (retryCount == 5) {
                log.info("消息异常，nack 消息到死信队列");
                channel.basicNack(tag, false, false);
            }
        }
    }

    @RabbitListener(queues = ConfirmConfig.DEAD_QUEUE_NAME, ackMode = "AUTO")
    public void receiveDead(Message message) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("死信队列接收到的消息是：{}", msg);
    }

}
