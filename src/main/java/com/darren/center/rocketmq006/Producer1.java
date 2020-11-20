package com.darren.center.rocketmq006;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * tags + keys -> 过滤消息
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer1 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        // topic 消息将要发送的地址
        // body 消息中具体的数据
        Message msg = new Message("myTopic003", "tag_aaa", "aaa", "aaa".getBytes());
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
        producer.shutdown();
    }

}
