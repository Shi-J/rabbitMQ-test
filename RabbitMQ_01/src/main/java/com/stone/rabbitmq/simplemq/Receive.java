package com.stone.rabbitmq.simplemq;

import com.rabbitmq.client.*;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//  接收信息  消费者
public class Receive
{
    private static final String QUEUE_NAME="simplemq_queue";

    public static void main(String[] args) throws IOException, TimeoutException
    {
        //  获取链接
        Connection connection = ConnectionUtils.getConnection();

        //  创建管道
        Channel channel = connection.createChannel();

        //  队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //  获取消息
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel)
        {
            //  handleDelivery
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException
            {

                String msg = new String(body, "utf-8");

                System.out.println("Receive...." + msg);
            }
        };

        //  消费消息，监听队列
        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);




    }
}
