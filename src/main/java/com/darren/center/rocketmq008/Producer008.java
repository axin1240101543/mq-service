package com.darren.center.rocketmq008;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h3>mq-service</h3>
 * <p>生产者</p>
 * 事务消息
 * 两阶段提交
 * @author : Darren
 * @date : 2020年09月28日 09:29:47
 **/
public class Producer008 {

    public static void main(String[] args) throws MQClientException{

        //使用TransactionMQProducer事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("xxoo");
        //设置name server地址
        producer.setNamesrvAddr("192.168.244.8:9876");
        //设置线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20,
                20L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));
        producer.setExecutorService(threadPoolExecutor);

        producer.setTransactionListener(new TransactionListener() {
            /**
             * 半消息发送成功触发此方法来执行本地事务
             *
             * LocalTransactionState.COMMIT_MESSAGE
             * 执行事务成功，确认提交
             *
             * LocalTransactionState.ROLLBACK_MESSAGE
             * 回滚消息，broker端会删除半消息
             *
             * LocalTransactionState.UNKNOW
             * 暂时为未知状态，等待broker回查
             * @param message
             * @param o
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    //执行本地事务
                    System.out.println("executeLocalTransaction");
                    System.out.println("getBody():" + new String(message.getBody()));
                    System.out.println("getTransactionId:" + message.getTransactionId());
                    System.out.println("Object:" + o);
                    return LocalTransactionState.COMMIT_MESSAGE;
                }catch (Exception e){
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            /**
             * broker将发送检查消息来检查事务状态，并将调用此方法来获取本地事务状态
             * ##### 本地事务执行状态
             *
             * LocalTransactionState.COMMIT_MESSAGE
             * 执行事务成功，确认提交
             *
             * LocalTransactionState.ROLLBACK_MESSAGE
             * 回滚消息，broker端会删除半消息
             *
             * LocalTransactionState.UNKNOW
             * 暂时为未知状态，等待broker回查
             * @param messageExt
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                // broker回调 检查事务

                System.out.println("checkLocalTransaction");
                System.out.println("getBody():" + new String(messageExt.getBody()));
                System.out.println("getTransactionId:" + messageExt.getTransactionId());

                //消息提交
                return LocalTransactionState.COMMIT_MESSAGE;
                //消息回滚
                //return LocalTransactionState.ROLLBACK_MESSAGE;
                //等一会儿
                //return LocalTransactionState.UNKNOW;
            }
        });

        producer.start();
        Message message = new Message("transaction-message-001", "message001".getBytes());
        TransactionSendResult transaction = producer.sendMessageInTransaction(message, "transaction");
        System.out.println(transaction);
        //producer.shutdown();
    }

}
