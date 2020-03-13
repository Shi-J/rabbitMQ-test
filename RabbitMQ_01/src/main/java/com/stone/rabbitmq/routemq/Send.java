package com.stone.rabbitmq.routemq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

public class Send
{
    //  交换机名称
    private static final String EXCHANGE_NAME="exchange_direct";
    public static void main(String[] args)
    {
        //  1.获取链接
        Connection connection = null;
        Channel channel = null;
        try
        {
            connection = ConnectionUtils.getConnection();

            // 2.创建管道
            channel = connection.createChannel();

            //  3.声明交换机 exchange 交换机名   direct 路由模式,需要用key进行比较，匹配成功在转发
            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            //  4.编辑消息和发送消息
            String msg = "subscibe mq...";
            //  设置路由key 需要匹配的key
            String routingKey ="info";
            channel.basicPublish(EXCHANGE_NAME,routingKey,null,msg.getBytes());

            System.out.println("send  direct ..." + msg);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            //  6.关闭资源
            if(channel!= null){
                try
                {
                    channel.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            if(connection!= null){
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
