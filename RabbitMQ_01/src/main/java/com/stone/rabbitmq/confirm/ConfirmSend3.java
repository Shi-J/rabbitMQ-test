package com.stone.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//  confirm模式检查消息是否到达服务器

//  异步模式的方法
public class ConfirmSend3
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

            //  存放未确认的消息的标识
            final SortedSet<Object> confirmSet= Collections.synchronizedSortedSet(new TreeSet<>());

            //  添加监听
            channel.addConfirmListener(new ConfirmListener()
            {
                //  成功调用
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException
                {
                    if (multiple){
                        System.out.println("handleAck---- \t 方法执行");
                        confirmSet.headSet(deliveryTag+1).clear();
                    }else{
                        System.out.println("handleAck---- \t 方法失败");
                        confirmSet.remove(deliveryTag);
                    }

                }

                //  失败调用
                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException
                {

                    if (multiple){
                        System.out.println("handleNack==== \t 方法执行");
                        confirmSet.headSet(deliveryTag+1).clear();
                    }else{
                        System.out.println("handleNack==== \t 方法失败");
                        confirmSet.remove(deliveryTag);
                    }
                }
            });

            //  编辑消息，发送消息
            String msg = "asynchronization   Send  confirm ...";

            for (int i = 0; i <100 ; i++)
            {
                long longNo=channel.getNextPublishSeqNo();
                //  发送消息
                channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
                //  添加消息到集合中
                confirmSet.add(longNo);
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
