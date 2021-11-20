package com.xulei.util.mqutil.service;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:39
 */
@Component
//@RabbitListener(bindings = {},queues = "${spring.rabbitmq.template.default-receive-queue}")
public class RabbiMqConsumer {
//value: @Queue 注解，用于声明队列，value 为 queueName, durable 表示队列是否持久化, autoDelete 表示没有消费者之后队列是否自动删除
//exchange: @Exchange 注解，用于声明 exchange， type 指定消息投递策略，我们这里用的 topic 方式
//key: 在 topic 方式下，这个就是我们熟知的 routingKey
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "b", durable = "false", autoDelete = "true"),
            exchange = @Exchange(value = "a", type = ExchangeTypes.TOPIC),
            key = "c"))
    public void process(Object o) {
        System.out.println(o);
    }
}
