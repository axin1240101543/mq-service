package com.darren.center.rocketmq002;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 批量发送消息 + 同步消息
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer002 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        // topic 消息将要发送的地址
        // body 消息中具体的数据
        Message msg1 = new Message("myTopic001", "hello1".getBytes());
        Message msg2 = new Message("myTopic001", "hello2".getBytes());
        Message msg3 = new Message("myTopic001", "hello3".getBytes());
        //同步消息发送
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(msg1);
        messages.add(msg2);
        messages.add(msg3);
        //发送多条消息（批量发送）
        /**
         * - 批量消息要求必要具有同一topic、相同消息配置
         * - 不支持延时消息
         * - 建议一个批量消息最好不要超过1MB大小
         * - 如果不确定是否超过限制，可以手动计算大小分批发送
         */
        SendResult sendResult = producer.send(messages);
        System.out.println("sendResult:" + sendResult);

        producer.shutdown();
    }

}
