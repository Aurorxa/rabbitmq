package com.github;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 消费者1
 *
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-25 09:02:24
 */
public class Consumer1 {
    /**
     * 普通交换机
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    /**
     * 普通队列
     */
    public static final String NORMAL_QUEUE = "normal_queue";

    /**
     * 普通交换机和普通队列之间的routing key
     */
    public static final String NORMAL_ROUTING_KEY = "normal";

    /**
     * 死信交换机
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";

    /**
     * 死信队列
     */
    public static final String DEAD_QUEUE = "dead_queue";

    /**
     * 死信交换机和死信队列之间的routing key
     */
    public static final String DEAD_ROUTING_KEY = "dead";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        /* 声明普通交换机 direct 模式 */
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        /* 声明普通队列 */
        // 为什么要设置 arguments , 是因为当消息成为死信之后，这里的普通队列就相当于生产者，而生产者需要知道交换机的名称和 routing key
        // 至于为什么这么设置 x-message-ttl x-dead-letter-exchange x-dead-letter-routing-key，后面有解释。
        Map<String, Object> arguments = MapUtil.newHashMap();
        // 设置 TTL 时间，单位是 ms ，一般是由生产者来设置，不然就会写死。
        // arguments.put("x-message-ttl", 10 * 1000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信交换机的routing key
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);

        /* 绑定普通交换机和普通队列 */
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);

        /* 声明死信交换机  direct 模式 */
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        /* 声明死信队列 */
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        /* 绑定死信交换机和死信队列 */
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {

            String msg = new String(message.getBody(), StandardCharsets.UTF_8);

            if (msg.contains("1")) { // 拒收消息
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else if (msg.contains("2")) {
                channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
            }

        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, (consumerTag) -> {});
    }
}
