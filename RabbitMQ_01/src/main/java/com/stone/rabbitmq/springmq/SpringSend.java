package com.stone.rabbitmq.springmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringSend
{
    public static void main(String[] args) throws Exception
    {
        //  获取spring整合rabbitMQ的配置
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-rabbitMQ.xml");
        //  RabbitMQ模板
        RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        //  使用RabbitMQ模板 发送消息
        template.convertAndSend("Hello, world , Spring rabbitMQ!");
        //  休眠1秒
        Thread.sleep(1000);
        //  spring容器销毁
        ctx.destroy();
    }
}
