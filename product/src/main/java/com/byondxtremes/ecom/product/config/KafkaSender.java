package com.byondxtremes.ecom.product.config;

import com.byondxtremes.ecom.common.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object message, String topicName) {
        kafkaTemplate.send(topicName, JsonUtil.convertToString(message));
        System.out.println("Message sent to: " + topicName);
    }



}
