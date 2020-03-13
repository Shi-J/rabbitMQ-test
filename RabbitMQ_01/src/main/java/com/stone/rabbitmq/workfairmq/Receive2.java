package com.stone.rabbitmq.workfairmq;

import com.rabbitmq.client.*;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;

//  多个消费者
public class Receive2
{
    private static final String QUEUE_NAME="workmq_queue";

    public static void main(String[] args) throws Exception
    {
        //  1.获取链接
        Connection connection = ConnectionUtils.getConnection();

        //  2.获取通道
        Channel channel  = connection.createChannel();

        //  3.声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //  默认为轮询分发,改为公平分发,
        channel.basicQos(1); // 保证每次只分发一个

        //  4.创建消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel)
        {
            //  消息到达触发该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope ,
                    AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String msg=new String(body,"utf-8");

                System.out.println("[2] receive..."+msg);

                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }finally
                {
                    System.out.println("[2] 结束");
                    //  接收消息结束之后手动回值
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }

            }
        };

        //  消费消息
        boolean autoAck=false; //自动应答
        channel.basicConsume(QUEUE_NAME,autoAck,defaultConsumer);

    }
}
