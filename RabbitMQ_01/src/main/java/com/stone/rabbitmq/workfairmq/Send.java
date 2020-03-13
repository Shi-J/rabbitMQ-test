package com.stone.rabbitmq.workfairmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;


//  一个消费者
public class Send
{

    private static final String QUEUE_NAME="workmq_queue";

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

            //  3.声明队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            //  4.创建多条消息
            for (int i = 0; i <50 ; i++)
            {
                String msg="work send..." +(i+1);

                //  5.发布消息
                channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

                Thread.sleep(100); //   停留一百毫秒

                System.out.println(msg); // 打印便于查看

            }

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
