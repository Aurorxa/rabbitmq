package com.github.callback;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-27 13:51:05
 */
@Value
@Slf4j
@Component
public class ReturnCallback implements RabbitTemplate.ReturnsCallback {

    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(this);
        /*
        * 如果 mandatory = true ，表示交换机在无法将消息进行路由的时候，会将该消息返回给生产者。
        * 如果 mandatory = false ，表示发现消息无法进行路由，则直接丢失。
        * */
        rabbitTemplate.setMandatory(true);
    }

    /**
     * 在消息传递过程中不可达目的地的时候，将消息返回给生产者。
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息 {} 被交换机 {} 给退回了，退回的原因是：{} ，路由键是：{}", new String(returned.getMessage().getBody(), StandardCharsets.UTF_8), returned.getExchange(), returned.getReplyText(), returned.getRoutingKey());
    }
}
