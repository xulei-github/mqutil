package com.xulei.util.mqutil.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:39
 */
@Component

public class ActiveMqConsumer {

    @JmsListener(destination = "amq_topic")
    public void process(String msg) {
        System.out.println(msg);
    }
}
