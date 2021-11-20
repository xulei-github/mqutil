package com.xulei.util.mqutil.aspect;

import com.alibaba.fastjson.JSONObject;
import com.xulei.util.mqutil.anno.RocketMqBean;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class RocketMqAspect {

    @Resource
    RocketMQTemplate rocketMQTemplate;

    ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(64);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 64, 10, TimeUnit.MILLISECONDS, arrayBlockingQueue);

    @Pointcut("@annotation(com.xulei.util.mqutil.anno.RocketMqBean)")
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
        RocketMqBean rocketMqBean = null;
        for (Method method : methods) {

            rocketMqBean = method.getAnnotation(RocketMqBean.class);
            if (rocketMqBean != null) {
                try {
                    //调用方法
                    invokeMethod(args, rocketMqBean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (MQClientException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void invokeMethod(Object[] obj, RocketMqBean rocketMqBean) throws IllegalAccessException, InstantiationException, MQClientException {

        executor.execute(() -> {
//            rocketMQTemplate.setDefaultDestination();
            System.out.println("rocketMQTemplate推送数据" + JSONObject.toJSONString(obj));
            if (!rocketMqBean.topic().isEmpty()) {
                if (!rocketMqBean.tags().isEmpty()) {
                    //指定topic，tag
                    rocketMQTemplate.convertAndSend(rocketMqBean.topic() + ":" + rocketMqBean.tags(), JSONObject.toJSONString(obj));
                } else {
                    //指定topic
                    rocketMQTemplate.convertAndSend(rocketMqBean.topic(), JSONObject.toJSONString(obj));
                }
            } else {
                rocketMQTemplate.convertAndSend(JSONObject.toJSONString(obj));
            }
        });
    }
}
