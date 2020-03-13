package com.stone.rabbitmq.springmq;
//  消费者
public class SpringReceive
{
    //消费方法
    public void consume(String str){
        System.out.println("消费者开始消费 ： "+str);
    }
}
