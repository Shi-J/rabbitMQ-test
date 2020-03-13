package com.stone.rabbitmq.routemq;

import com.rabbitmq.client.*;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;

public class Receive
{

    //  设置队列的名字
    private static final String QUEUE_NAME="routemq_queue_direct_1";

    //  交换机名称 用于和队列绑定
    private static final String EXCHANGE_NAME="exchange_direct";
    public static void main(String[] args) throws Exception
    {

        //  1.获取连接
        Connection connection = ConnectionUtils.getConnection();

        //  2.创建管道
        Channel channel = connection.createChannel();

        //  3.队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //  4.绑定队列到交换机  routingKey需要匹配的key
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");

        //  默认为轮询分发,改为公平分发,
        channel.basicQos(1); // 保证每次只分发一个

        //  5.创建消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel)
        {
            //  消息到达触发该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope ,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String msg=new String(body,"utf-8");

                System.out.println("[1] routemq_queue_direct_1..."+msg);

                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }finally
                {
                    System.out.println("[1] 结束");
                    //  接收消息结束之后手动回值
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }

            }
        };

        //  消费消息
        boolean autoAck=false; //自动应答
        channel.basicConsume(QUEUE_NAME,false,defaultConsumer);
    }
}
