package com.catalog.Publisher;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.catalog.service.ProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SNSPublisher {

    @Autowired
    private AmazonSNSClient amazonSNSClient;

    private Logger logger = LoggerFactory.getLogger(ProductCatalogService.class);

    public void publishMessageToTopic(String topicARN, String message) {
        PublishRequest request = new PublishRequest(topicARN, message);

        PublishResult result = this.amazonSNSClient.publish(request);
        logger.debug(String.format("SNS Message Sent with messageId: %s", result.getMessageId()));
    }
}
