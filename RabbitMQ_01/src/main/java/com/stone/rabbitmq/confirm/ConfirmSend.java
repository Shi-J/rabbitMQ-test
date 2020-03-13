package com.stone.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//  confirm模式检查消息是否到达服务器

//  普通模式的方法
public class ConfirmSend
{

    //  队列名称
    private static final String QUEUE_NAME = "queue_confirm";

    public static void main(String[] args)
    {
        Connection connection= null;
        Channel channel=null;

        try
        {
            //  获取连接
            connection = ConnectionUtils.getConnection();
            //  创建管道
            channel=connection.createChannel();
            //  创建队列声明
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //  生产者设置confirm模式
            channel.confirmSelect();
            //  编辑消息，发送消息
            String msg = "Send  confirm ...";
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

            //  普通模式发送一条触发  效率低
            if(!channel.waitForConfirms()){
                System.out.println("confirm 发送失败");
            }else{
                System.out.println("confirm 发送ok");
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally
        {
            //  关闭资源
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
