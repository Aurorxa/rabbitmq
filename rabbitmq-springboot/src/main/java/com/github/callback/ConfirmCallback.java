package com.github.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-27 13:51:05
 */
@Slf4j
@Component
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * 不管交换机是否收到消息都会触发这个回调方法
     *
     * @param correlationData 回调的相关数据
     * @param ack             ack
     * @param cause           回调的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = Optional.ofNullable(correlationData).orElseGet(CorrelationData::new).getId();
        if (ack) {
            log.info("交换机已经接收到 id 为 {} 的消息", id);
        } else {
            log.info("交换机未接收到 id 为 {} 的消息，原因是：{}", id, cause);
        }
    }
}
