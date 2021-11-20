package com.xulei.util.mqutil.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:39
 */
@Component

public class KafkaConsumer {

    //    @KafkaListener(topics = {"${spring.kafka.template.default-topic}"})
    @KafkaListener(topics = {"abc"})
    public void process(ConsumerRecord<?,?> record) {
        System.out.println(record);
    }
}
