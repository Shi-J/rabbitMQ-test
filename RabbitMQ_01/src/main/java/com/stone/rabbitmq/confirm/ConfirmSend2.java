package com.stone.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.stone.rabbitmq.util.ConnectionUtils;

import java.io.IOException;

//  confirm模式检查消息是否到达服务器

//  普通模式的方法
public class ConfirmSend2
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
            String msg = "Send  confirm batch...";
            //  批量发送三十条
            try
            {
                for (int i = 0; i <30 ; i++)
                {
                    if(i ==5){
                        System.out.println(i/0);
                    }
                    channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
                }

                //  批量模式发送30条触发  比单条的效率高
                if(!channel.waitForConfirms()){
                    System.out.println("confirm 发送失败");
                }else{
                    System.out.println("confirm 发送ok");
                }
            }
            catch (Exception e)
            {
                System.out.println("confirm 发送失败");
                e.printStackTrace();
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
