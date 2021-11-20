package com.xulei.util.mqutil.service;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:39
 */
@Component
@RocketMQMessageListener(topic = "topic1", selectorExpression = "tag1", consumeMode = ConsumeMode.ORDERLY, consumerGroup = "${rocketmq.consumer.group}")
public class RocketMqConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("接收到消息：" + s);
    }
}
