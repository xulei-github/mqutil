package com.xulei.util.mqutil.controller;

import com.xulei.util.mqutil.anno.RocketMqBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:04
 */
@RestController
public class RocketMqSendController {

    @RocketMqBean(topic = "topic1", tags = "tag1")
    @RequestMapping("send/rocketmq")
    public void sendMqMsg(@RequestBody Object o) {

    }
}
