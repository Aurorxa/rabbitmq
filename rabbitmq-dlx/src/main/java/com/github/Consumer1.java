package com.github;

import cn.hutool.core.map.MapUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.util.HashMap;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-05-24 09:51:41
 */
public class Consumer1 {

    /**
     * 普通交换机的名称
     */
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    /**
     * 普通队列的名称
     */
    public static final String NORMAL_QUEUE = "normal_queue";

    /**
     * routing key
     */
    public static final String NORMAL_ROUTING_KEY = "normal";

    /**
     * 死信交换机的名称
     */
    public static final String DEAD_EXCHANGE = "dead_exchange";

    /**
     * 死信队列的名称
     */
    public static final String DEAD_QUEUE = "dead_queue";

    /**
     * routing key
     */
    public static final String DEAD_ROUTING_KEY = "dead";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();

        // 声明普通换机和死信交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明普通队列
        HashMap<String, Object> arguments = MapUtil.newHashMap();
        // 设置过期时间，实际开发中，可以在生产者那边设置，更加灵活
        // arguments.put("x-message-ttl", 10 * 1000);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信 routing key ，因为此时的普通队列相当于生产者（生产者需要知道死信队列的交换机和死信队列的 routing key）
        arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        // 设置正常队列的长度限制
        // arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);
        // 死信队列
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        // 绑定交换机和队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);

        System.out.println("----------- 绑定关系完成 -----------");

    }
}
