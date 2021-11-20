package com.xulei.util.mqutil.aspect;

import com.alibaba.fastjson.JSONObject;

import com.xulei.util.mqutil.anno.KafkaBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
public class KafkaAspect {

    @Autowired
    KafkaTemplate kafkaTemplate;

    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(64);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 64, 10, TimeUnit.MILLISECONDS, arrayBlockingQueue);

    @Pointcut("@annotation(com.xulei.util.mqutil.anno.KafkaBean)")
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

        KafkaBean kafkaBean = null;
        Method[] methods = declaringType.getMethods();
        for (Method method : methods) {
            kafkaBean = method.getAnnotation(KafkaBean.class);
            if (kafkaBean != null) {
                try {
                    //调用方法
                    invokeMethod(args, kafkaBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void invokeMethod(Object[] obj, KafkaBean kafkaBean) throws IllegalAccessException, InstantiationException {
        if (!kafkaBean.defaultTopic().isEmpty()) {
            kafkaTemplate.setDefaultTopic(kafkaBean.defaultTopic());
        }
        executor.execute(() -> {
            System.out.println("kafkaTemplate推送数据" + JSONObject.toJSONString(obj));
            kafkaTemplate.sendDefault(JSONObject.toJSONString(obj));
        });
    }
}
