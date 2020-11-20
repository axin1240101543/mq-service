package com.darren.center.rocketmq001;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 简单demo + 同步消息
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer001 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        // topic 消息将要发送的地址
        // body 消息中具体的数据
        Message msg = new Message("myTopic001", "hello".getBytes());
        //同步消息发送
        SendResult sendResult = producer.send(msg);
        System.out.println("sendResult:" + sendResult);

        producer.shutdown();
    }

}
