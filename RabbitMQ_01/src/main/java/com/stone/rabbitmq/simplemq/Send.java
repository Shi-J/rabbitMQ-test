package com.stone.rabbitmq.simplemq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//  发送信息    生产者
public class Send
{
    private static final String QUEUE_NAME="simplemq_queue";

    public static void main(String[] args) throws IOException, TimeoutException
    {
        //  获取链接
        Connection connection = ConnectionUtils.getConnection();

        //  创建通道,从链接中获取
        Channel channel = connection.createChannel();

        //  创建队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //  编辑消息内容
        String msg = "hello rabbitmq !!";

        //  发送消息
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        System.out.println("send ---" +msg);

        //  关闭链接
        channel.close();
        connection.close();

    }
}
