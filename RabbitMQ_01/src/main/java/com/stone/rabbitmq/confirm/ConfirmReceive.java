package com.stone.rabbitmq.confirm;

import com.rabbitmq.client.*;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;

public class ConfirmReceive
{
    //  队列名称
    private static final String QUEUE_NAME = "queue_confirm";
    private static int i =1;
    public static void main(String [] agrs) throws Exception{
        //  获取驱动
        Connection connection = ConnectionUtils.getConnection();
        //  创建管道
        Channel channel = connection.createChannel();

        //  声明队列 获取消息同步完成
        channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
            //  消息到底之后会转入到该方法中
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                //  查看消息
                System.out.println("ConfirmReceive....." +new String(body,"utf-8")+(i++));
            }
        });



    }
}
