package com.darren.center.rocketmq009;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 重试机制
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer009 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        /**
         * 默认超时时间 sendMsgTimeout 3000毫秒
         * 同步发送时 重试次数，默认 2
         */
        producer.setRetryTimesWhenSendFailed(2);
        /**
         * 默认超时时间 sendMsgTimeout 3000毫秒
         * 异步发送时 重试次数，默认 2
         */
        producer.setRetryTimesWhenSendAsyncFailed(2);
        /**
         * 是否向其他broker发送请求 默认false
         */
        producer.setRetryAnotherBrokerWhenNotStoreOK(false);
        producer.start();
        Message message = new Message("message-001", "message001".getBytes());
        SendResult sendResult = producer.send(message);
        System.out.println(sendResult);
        //producer.shutdown();
    }

}
