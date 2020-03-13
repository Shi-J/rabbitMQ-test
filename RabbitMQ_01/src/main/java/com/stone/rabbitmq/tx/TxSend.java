package com.stone.rabbitmq.tx;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//  通过事务模式，查看事务是否回滚来检查消息是否到达服务器   AMQP模式，降低了rabbitMQ的吞吐量
public class TxSend
{
    //  队列名称
    private static final String QUEUE_NAME = "queue_tx";

    public static void main(String[] args)
    {
        Connection connection = null;
        Channel channel = null;

        try
        {
            //  创建连接
            connection = ConnectionUtils.getConnection();

            //   获取管道
            channel = connection.createChannel();

            //   创建队列声明
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

//          //  txSelect开启事务模式
            channel.txSelect();

            //  编辑消息发送消息
            String msg = "Send queue tx....";
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
//            System.out.println(1/0);
            //  txCommit提交事务
            channel.txCommit();
            System.out.println("send tx ..." + msg);


        }
        catch (Exception e)
        {
            //  如果出现异常进行回滚操作
            try
            {
                channel.txRollback();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }finally
            {
                System.out.println("处理失败 ，事务回滚");
            }
            e.printStackTrace();
        }
        finally
        {
            //  6.关闭资源
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
