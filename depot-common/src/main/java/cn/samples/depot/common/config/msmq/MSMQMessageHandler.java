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
//package cn.samples.depot.common.config.msmq;
//
//import cn.samples.depot.common.model.msmq.ManifestBase;
//import cn.samples.depot.common.utils.XstreamUtil;
//import ionic.Msmq.Message;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.UnsupportedEncodingException;
//
///**
// * Handles the in coming message context retrieved via the MSMQ listener
// */
//public class MSMQMessageHandler {
//    protected Logger log = LoggerFactory.getLogger(MSMQMessageHandler.class);
//
//    protected String destinationQueueName = null;
//    // TODO 入库 service
//
//    public MSMQMessageHandler(String destination) {
//        this.destinationQueueName = destination;
//    }
//
//    public boolean onMessage(Message message) {
//        if (log.isDebugEnabled()) {
//            StringBuffer sb = new StringBuffer();
//            sb.append("Received a new MSMQ message for queue: ").append(destinationQueueName);
//            sb.append("\nDestination     : ").append(destinationQueueName);
//            try {
//                sb.append("\nCorrelation ID  : ").append(message.getCorrelationIdAsString());
//                sb.append("\nMessage         :").append(message.getBodyAsString());
//            } catch (UnsupportedEncodingException e) {
//                log.error("Unsupported message received: ", e);
//            }
//        }
//        boolean sucessful = false;
//        try {
//            log.info("接收到的msmq的消息内容为：{}", message.getBodyAsString());
//            sucessful = handleMessage(message);
//        } catch (Exception e) {
//            log.error("Cloud not pocess the message: {}", e);
//        }
//        return sucessful;
//    }
//
//    /**
//     * Set up message properties to header as it received as MSMQ message properties
//     *
//     * @param message
//     * @return
//     */
//    public boolean handleMessage(Message message) throws Exception {
//        // Get the label from the MSMQ message
//        String label = message.getLabel();
//        String body = message.getBodyAsString();
//        String correlationId = message.getCorrelationIdAsString();
//
//        if (log.isDebugEnabled()) {
//            log.debug("message label : {}, correlationId : {}, body : {}", label, correlationId, body);
//            log.debug("model : {}", XstreamUtil.parseXMLWithRootName(label, body, ManifestBase.class));
//        }
//
//        // TODO: handle message
//
//        return true;
//    }
//}
