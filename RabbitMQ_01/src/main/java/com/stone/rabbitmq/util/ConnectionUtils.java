package com.stone.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils
{
    /*
        1.获取mq的连接
     */

    public static Connection getConnection() throws IOException, TimeoutException
    {
        //  1.1定义一个连接工厂
        ConnectionFactory factory =new ConnectionFactory();

        //  1.2设置服务地址
        factory.setHost("127.0.0.1");

        //  AMQP：5672  1.3 设置端口
        factory.setPort(5672);

        //  1.4 设置数据库 也就是mq的Virtual Hosts
        factory.setVirtualHost("/hosts_shij");

        //  1.5 用户名
        factory.setUsername("shij");

        //  1.6 密码
        factory.setPassword("123456");
        
        //  1.7获取连接
        Connection connection = factory.newConnection();

        return  connection;
    }
}
