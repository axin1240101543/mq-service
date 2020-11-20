package com.darren.center.rocketmq010;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * <h3>mq-service</h3>
 * <p>消费者</p>
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Consumer010_002 {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("aaa");
        consumer.setNamesrvAddr("192.168.244.8:9876");

        //每个consumer关注一个topic
        //topic 关注的消息地址
        //过滤器 * 表示不过滤
        consumer.subscribe("message-001", "*");


        //注册监听器（并发消费 -> 一个queue开启多个线程）
        /*consumer.registerMessageListener(new MessageListenerConcurrently(){

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                list.forEach(message -> {
                    byte[] body = message.getBody();
                    System.out.println(new String(body));
                });
                // 返回一个消费状态 CONSUME_SUCCESS:消费成功 RECONSUME_LATER：稍后重新推送消费
                // 默认情况下 只会被一个consumer消费到 点对点消费
                // ack -> acknowledge
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });*/

        /**
         * 最大开启消费线程数
         */
        consumer.setConsumeThreadMax(1);
        /**
         * 最小开启消费线程数
         */
        consumer.setConsumeThreadMin(1);

        /**
         * MessageListenerOrderly 顺序消费 对一个queue开启一个线程，多个queue则开多个线程
         */
        consumer.registerMessageListener(new MessageListenerOrderly(){
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                list.forEach(message -> {
                    byte[] body = message.getBody();
                    System.out.println("threadName:"  +  Thread.currentThread().getName() + " message:" + new String(body));
                });
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        /**
         * 集群： 一组consumer中一个消费就可以了，失败会重投，不保证重投到原来的consumer，消费状态由broker维护
         * 广播： 每个consumer都能消费，失败不会重投，消费状态由consumer维护
         */
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.start();
        System.out.println("consumer1 started");
    }

}
