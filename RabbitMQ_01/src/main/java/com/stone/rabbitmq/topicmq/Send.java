package com.stone.rabbitmq.topicmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send
{
    //  交换机名称
    private static final String EXCHANGE_NAME = "exchange_topic";

    public static void main(String[] agrs)
    {
        Connection connection = null;
        Channel channel = null;

        try
        {
            //  1.获取驱动
            connection = ConnectionUtils.getConnection();

            //  2.创建管道
            channel = connection.createChannel();

            //  3.声明交换机 exchange 交换机名   topic 主题模式
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            //  4.编辑消息和发送消息  goods.add商品添加
            String msg = "操作商品......";
            channel.basicPublish(EXCHANGE_NAME,"goods.add",null,msg.getBytes());

            System.out.println("topic  send ...." +msg);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //  5.关闭资源
            if (channel != null)
            {
                try
                {
                    channel.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
}
