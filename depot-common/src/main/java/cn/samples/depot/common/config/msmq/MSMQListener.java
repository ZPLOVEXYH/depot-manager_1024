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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * The revamped MSMQ Transport listener implementation. Creates {@link MSMQReceiverManager} instances
// * for each service requesting exposure over MSMQ, and stops these if they are undeployed / stopped.
// * <p>
// * A service indicates a MSMQ client connection  definition by name, which would be defined in the
// * MSMQListener on the axis2.xml, and this provides a way to reuse common configuration between
// * services, as well as to optimize resources utilized
// * All Destinations / MSMQ connection  objects used MUST be pre-created or already available
// */
//public class MSMQListener {
//    private Logger log = LoggerFactory.getLogger(MSMQListener.class);
//
//    private String msmqHost, msmqQueue;
//    private int timeout, consumer = 1;
//
//    private ExecutorService workerPool;
//    private MSMQReceiverManager srm;
//
//    public MSMQListener(String msmqHost, String msmqQueue, int timeout) {
//        this(msmqHost, msmqQueue, timeout, 1);
//    }
//
//    public MSMQListener(String msmqHost, String msmqQueue, int timeout, int consumer) {
//        this.msmqHost = msmqHost;
//        this.msmqQueue = msmqQueue;
//        this.timeout = timeout;
//        this.consumer = consumer;
//    }
//
//    /**
//     * Listen for MSMQ messages on behalf of the given service
//     */
//    public void start(MSMQMessageHandler messageHandler) throws Exception {
//        String destinationQueue = getReceiverQueueFullName(msmqQueue);
//        workerPool = Executors.newCachedThreadPool();
//        srm = new MSMQReceiverManager(destinationQueue, timeout, consumer, workerPool);
//        srm.setMsmqMessageHandler(messageHandler);
//        srm.start();
//        // just wait three seconds to start the poll task
//        for (int i = 0; i < 3; i++) {
//            if (srm.getActiveTaskCount() > 0) {
//                if (log.isDebugEnabled()) {
//                    log.info("Started to listen on destination : " + destinationQueue);
//                }
//                return;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                //ignore the exception
//            }
//        }
//        stop();
//        log.warn("Polling tasks on destinatination :" + destinationQueue + " has not yet started" +
//                "after 3 seconds");
//    }
//
//
//    /**
//     * Stops listening for messages for the service thats undeployed or stopped
//     */
//    public void stop() {
//        if (null != srm) srm.stop();
//        if (null != workerPool) workerPool.shutdown();
//        if (log.isInfoEnabled()) {
//            log.info("Stopped listening for MSMQ messages on destinatination : " + msmqQueue);
//        }
//    }
//
//    /**
//     * Generating full name (standard MSMQ full path which is listen to the MSMQ service)
//     *
//     * @param queueName
//     * @return
//     */
//    public String getReceiverQueueFullName(String queueName) {
//        String h1 = msmqHost;
//        String a1 = "OS";
//        if ((h1 == null) || h1.equals(""))
//            h1 = ".";
//        char[] c = h1.toCharArray();
//        if ((c[0] >= '1') && (c[0] <= '9'))
//            a1 = "TCP";
//
//        return "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueName;
//    }
//
//}
