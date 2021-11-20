package com.xulei.util.mqutil.controller;

import com.xulei.util.mqutil.anno.ActiveMqBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xulei30
 * @Date: 2021/11/18 10:04
 */
@RestController
public class ActiveSendController {

    @ActiveMqBean(destination = "amq_topic")
    @RequestMapping("send/activemq")
    public void sendMqMsg(@RequestBody Object o){

    }
}
