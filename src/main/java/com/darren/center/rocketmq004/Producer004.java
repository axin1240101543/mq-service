package com.darren.center.rocketmq004;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 单向消息。不需要确认
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer004 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        // topic 消息将要发送的地址
        // body 消息中具体的数据
        Message msg = new Message("myTopic001", "hello1".getBytes());
        /**
         * 只发送消息，不等待服务器响应，只发送请求不等待应答。
         * 此方式发送消息的过程耗时非常短，一般在微秒级别。
         */
        producer.sendOneway(msg);
        producer.shutdown();
    }

}
