package com.darren.center.rocketmq003;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 异步消息
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer003 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();

        // topic 消息将要发送的地址
        // body 消息中具体的数据
        Message msg = new Message("myTopic001", "hello1".getBytes());
        /**
         * 异步可靠消息
         * 不会阻塞等待broker的确认
         * 采用事件监听方式接受broker返回的确认
         */
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("onSuccess:" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                //处理异常，重投或者调整逻辑
                System.out.println("onException:" + throwable.getMessage());
            }
        });

        //发送异步消息的时候不能随便shutdown，需要等待broker回调
        // nException:org.apache.rocketmq.remoting.exception.RemotingConnectException:
        // connect to [192.168.244.8:9876] failed
        //producer.shutdown();
    }

}
