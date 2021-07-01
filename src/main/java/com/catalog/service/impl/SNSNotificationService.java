package com.catalog.service.impl;

import com.catalog.Publisher.SNSPublisher;
import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import com.catalog.service.NotificationService;
import com.catalog.service.ProductCatalogService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SNSNotificationService implements NotificationService {

    Logger logger = LoggerFactory.getLogger(ProductCatalogService.class);

    @Autowired
    private SNSPublisher publisher;

    @Value("${cloud.aws.end-point.sns}")
    private String ARN;

    @Override
    public void notifyDownstreamServices(EventCatalog eventName, Product product) {
        Gson gson = new Gson();
        HashMap<String, Object> message = new HashMap<>();
        message.put("event", eventName.getName());
        message.put("product", product);

        String jsonMessage = gson.toJson(message);

        try {
            this.publisher.publishMessageToTopic(ARN, jsonMessage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_SNS_ERROR);
        }
    }
}
