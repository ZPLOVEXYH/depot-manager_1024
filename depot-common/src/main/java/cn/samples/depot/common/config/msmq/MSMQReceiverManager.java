///*
// * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package cn.samples.depot.common.config.msmq;
//
//import ionic.Msmq.Message;
//import ionic.Msmq.MessageQueueException;
//import ionic.Msmq.Queue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//
///**
// * Each service will have one MSMQReceiverManager instance that will create, manage and also destroy
// * idle tasks created for it, for message receipt.
// * <p>
// * This also acts as the ExceptionListener for all MSMQ connections made on behalf of the service.
// * Since the ExceptionListener is notified by a JMS provider on a "serious" error, we simply try
// * to re-connect. Thus a connection failure for a single task, will re-initialize the state afresh
// * for the service, by discarding all connections.
// */
//public class MSMQReceiverManager {
//    private static final Logger log = LoggerFactory.getLogger(MSMQReceiverManager.class);
//
//    private static final int STATE_STOPPED = 0;
//    private static final int STATE_STARTED = 1;
//    private static final int STATE_PAUESED = 2;
//    private static final int STATE_SHUTTING_DOWN = 3;
//    private static final int STATE_FAILURE = 4;
//
//    private volatile int workerState = STATE_STOPPED;
//    private volatile int msmqTaskManagerState = STATE_STOPPED;
//
//    private volatile int activeTaskCount = 0;
//    private int timeout, consumer = 1;
//
//    private String destinationQueue;
//    private ExecutorService workerPool = null;
//    private MSMQMessageHandler msmqMessageHandler = null;
//    private Queue queue;
//    /**
//     * The list of active tasks thats managed by this instance
//     */
//    private final List<MessageListenerTask> pollingTasks = Collections.synchronizedList(new ArrayList<MessageListenerTask>());
//
//    public MSMQReceiverManager(String destinationQueue, int timeout, int consumer, ExecutorService workerPool) {
//        this.destinationQueue = destinationQueue;
//        this.workerPool = workerPool;
//        this.timeout = timeout;
//        this.consumer = consumer;
//    }
//
//    public void setMsmqMessageHandler(MSMQMessageHandler msmqMessageReceiver) {
//        this.msmqMessageHandler = msmqMessageReceiver;
//    }
//
//    public int getActiveTaskCount() {
//        return activeTaskCount;
//    }
//
//    public synchronized void start() {
//        // Separate sharable thread is initiating the work under the MSMQLinstener
//        for (int i = 0; i < consumer; i++) {
//            workerPool.execute(new MessageListenerTask());
//        }
//    }
//
//    /**
//     * Start or re-start the Task Manager by shutting down any existing worker tasks and
//     * re-creating them. However, if this is STM is PAUSED, a start request is ignored.
//     * This applies for any connection failures during paused state as well, which then will
//     * not try to auto recover
//     */
//    public synchronized void stop() {
//        if (log.isDebugEnabled()) {
//            log.debug("Stopping MSMQReceiverManager for queue : " + destinationQueue);
//        }
//
//        if (msmqTaskManagerState != STATE_FAILURE) {
//            msmqTaskManagerState = STATE_SHUTTING_DOWN;
//        }
//
//        synchronized (pollingTasks) {
//            for (MessageListenerTask lstTask : pollingTasks) {
//                lstTask.requestShutdown();
//            }
//        }
//
//        if (msmqTaskManagerState != STATE_FAILURE) {
//            msmqTaskManagerState = STATE_STOPPED;
//        }
//
//        queue = null;
//
//        synchronized (pollingTasks) {
//            pollingTasks.clear();
//        }
//
//        if (log.isInfoEnabled()) {
//            log.info("Task manager for queue : " + destinationQueue + " shutdown");
//        }
//    }
//
//    /**
//     * The actual threads/tasks that perform MSMQ message polling
//     */
//    private class MessageListenerTask implements Runnable {
//
//        protected void requestShutdown() {
//            workerState = STATE_SHUTTING_DOWN;
//        }
//
//        private boolean isActive() {
//            return workerState == STATE_STARTED;
//        }
//
//        /**
//         * As soon as we create a new polling task, add it to the STM for
//         * control later
//         */
//        MessageListenerTask() {
//            synchronized (pollingTasks) {
//                pollingTasks.add(this);
//            }
//        }
//
//        public void run() {
//            if (log.isInfoEnabled()) {
//                log.info("[[[MSMQ Listner has started...... MSMSQ QUEUE IS ]]]]:" + destinationQueue);
//            }
//            workerState = STATE_STARTED;
//            Message mqMessage = null;
//            try {
//                queue = new Queue(destinationQueue, Queue.Access.RECEIVE);
//            } catch (Exception e) {
//                log.error("Error while opening queue!" + destinationQueue);
//            }
//
//            if (log.isInfoEnabled()) {
//                log.info("Open the destination with the name: " + destinationQueue);
//            }
//            activeTaskCount++;
//            if (log.isDebugEnabled()) {
//                log.debug("New poll task starting: thread id = " + Thread.currentThread().getId());
//            }
//
//            // read messages from the queue and process them for forever..
//            try {
//                while (isActive() && queue != null) {
//                    try {
//                        mqMessage = queue.receive(timeout);
//                    } catch (MessageQueueException e) {
//                        // just ignore
//                    }
//                    if (log.isTraceEnabled()) {
//                        if (mqMessage != null) {
//                            log.trace("Read a message from : + " + destinationQueue + "by Thread ID : " + Thread.currentThread().getId());
//                        } else {
//                            log.trace("No message received by Thread ID : " + Thread.currentThread().getId() + " for destination : " +
//                                    destinationQueue);
//                        }
//                    }
//                    if (mqMessage != null) {
//                        handleMessage(mqMessage);
//                        mqMessage = null;
//                    }
//                }
//            } finally {
//                workerState = STATE_STOPPED;
//                activeTaskCount--;
//                synchronized (pollingTasks) {
//                    pollingTasks.remove(this);
//                }
//
//                try {
//                    if (null != queue) {
//                        queue.close();
//                    }
//                } catch (MessageQueueException e) {
//                    log.warn("Error while close queue! {}", destinationQueue);
//                }
//            }
//
//        }
//    }
//
//    private void handleMessage(Message msmqMessage) {
//        msmqMessageHandler.onMessage(msmqMessage);
//    }
//
//    private void handleException(String msg, Exception e) {
//        log.error(msg);
//        throw new RuntimeException(msg, e);
//    }
//
//}