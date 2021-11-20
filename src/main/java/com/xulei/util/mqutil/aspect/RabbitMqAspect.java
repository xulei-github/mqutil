package com.xulei.util.mqutil.aspect;

import com.alibaba.fastjson.JSONObject;
import com.xulei.util.mqutil.anno.RabbitMqBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xulei30
 * @Date: 2021/11/17 20:28
 */
@Aspect
@Component
public class RabbitMqAspect {


    @Autowired
    RabbitTemplate rabbitTemplate;

    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(64);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 64, 10, TimeUnit.MILLISECONDS, arrayBlockingQueue);

    @Pointcut("@annotation(com.xulei.util.mqutil.anno.RabbitMqBean)")
    public void mqAspect() {

    }

    @After(value = "mqAspect()")
    public void mqAfterPoint(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        //参数
        Object[] args = joinPoint.getArgs();
        //类
        Class declaringType = signature.getDeclaringType();
        //方法名
        String name = null;
        Method[] methods = declaringType.getMethods();
        RabbitMqBean rabbitMqBean = null;
        for (Method method : methods) {

            rabbitMqBean = method.getAnnotation(RabbitMqBean.class);
            if (rabbitMqBean != null) {
                try {
                    //调用方法
                    invokeMethod(args, rabbitMqBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void invokeMethod(Object[] obj, RabbitMqBean rabbitMqBean) throws IllegalAccessException, InstantiationException {
        if (!rabbitMqBean.defaultReceiveQueue().isEmpty()) {
            rabbitTemplate.setDefaultReceiveQueue(rabbitMqBean.defaultReceiveQueue());
        }
        if (!rabbitMqBean.exchange().isEmpty()) {
            rabbitTemplate.setExchange(rabbitMqBean.exchange());
        }
        if (!rabbitMqBean.routingKey().isEmpty()) {
            rabbitTemplate.setRoutingKey(rabbitMqBean.routingKey());
        }

        executor.execute(() -> {
            System.out.println("RabbitTemplate推送数据" + JSONObject.toJSONString(obj));
            rabbitTemplate.convertAndSend(JSONObject.toJSONString(obj));
        });
    }
}
