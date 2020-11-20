package com.darren.center.rocketmq007;


import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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
public class Consumer007 {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("aaa");
        consumer.setNamesrvAddr("192.168.244.8:9876");

        //过滤年龄大于等于18小于等于38的消息
        MessageSelector messageSelector = MessageSelector.bySql("age >= 18 and age <= 38");
        //每个consumer关注一个topic
        //topic 关注的消息地址
        //过滤器 * 表示不过滤
        consumer.subscribe("myTopic003", messageSelector);

        //注册监听器
        consumer.registerMessageListener(new MessageListenerConcurrently(){

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
