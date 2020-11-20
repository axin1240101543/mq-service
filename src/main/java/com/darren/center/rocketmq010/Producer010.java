package com.darren.center.rocketmq010;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByMachineRoom;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByRandom;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 *
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer010 {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        producer.start();


        for (int i = 0; i < 20; i++) {
            Message message = new Message("message-001", ("message" + i).getBytes());
            /**
             * queue选择器，想topic中的哪个queue写消息
             */
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {

                /**
                 * 手动选择一个queue
                 * @param list 当前的topic里面包含的queue列表
                 * @param message 具体要发的消息
                 * @param o 自定义参数 -> send方法中的arg
                 * @return 选择好queue
                 */
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    MessageQueue targetQueue = null;
                    list.forEach((queue) -> System.out.println(queue));
                    //向固定的一个queue里写消息
                    for (MessageQueue queue : list) {
                        if ((int)o == queue.getQueueId()){
                            targetQueue = queue;
                            break;
                        }
                    }
                    return targetQueue;
                }
            }, 1);

            /**
             * queue选择器默认实现
             *  Hash
             */
            //SendResult sendResult1 = producer.send(message, new SelectMessageQueueByHash(), "1");
            /**
             * queue选择器默认实现
             * Random
             */
            //SendResult sendResult2 = producer.send(message, new SelectMessageQueueByRandom(), "1");
            /**
             * queue选择器默认实现
             * 未实现
             * MachineRoom
             */
            //SendResult sendResult3 = producer.send(message, new SelectMessageQueueByMachineRoom(), "1");
            System.out.println("threadName:"  +  Thread.currentThread().getName() + " sendResult:" + sendResult);
        }
        producer.shutdown();
    }

}
