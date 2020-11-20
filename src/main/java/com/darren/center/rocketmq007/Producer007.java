package com.darren.center.rocketmq007;

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
 * SQL -> 过滤消息
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer007 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        ArrayList<Message> msgs = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            // topic 消息将要发送的地址
            // body 消息中具体的数据
            Message msg = new Message("myTopic003", "tag_aaa", "aaa", ("aaa" + i).getBytes());
            //消息的属性为age,一共100条消息， age为1-100
            msg.putUserProperty("age", String.valueOf(i));
            msgs.add(msg);
        }
        SendResult sendResult = producer.send(msgs);
        System.out.println(sendResult);
        producer.shutdown();
    }

}
