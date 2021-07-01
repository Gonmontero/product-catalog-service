package com.catalog.consumer;


import com.catalog.consumer.processor.DataProcessor;
import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;
import com.catalog.exception.ApplicationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

@Component
public class SQSConsumer implements Runnable {

    Logger logger = LoggerFactory.getLogger(SQSConsumer.class);

    @Value("${cloud.aws.end-point.sqs}")
    private String sqsUrl;

    @Autowired
    private DataProcessor<Product, EventCatalog> messageProcessor;

    @Autowired
    private SqsClient sqsClient;

    public void run() {
        while (true) {
            try {
                List<Message> messages = pollMessages();

                for (Message message : messages) {
                    if (validateMessage(message)) {
                        JSONObject json = new JSONObject(message.body());
                        String stringMessage = json.getString("Message");
                        json = new JSONObject(stringMessage);

                        JSONObject productJson = json.getJSONObject("product");
                        Product product = Product.Builder.Builder()
                                .withID(productJson.getString("id"))
                                .withName(productJson.getString("name"))
                                .withPrice(productJson.getBigDecimal("price"))
                                .withQuantity(productJson.getInt("quantity"))
                                .build();

                        EventCatalog event = EventCatalog.fromName(json.getString("event"));

                        try {
                            messageProcessor.processData(product, event);
                            deleteMessageFromQueue(message.receiptHandle());
                        } catch (ApplicationException e) {
                            logger.error(e.getMessage());
                        }
                    } else {
                        deleteMessageFromQueue(message.receiptHandle());
                    }
                }

                Thread.sleep(1000);
            } catch (SqsException e) {
                System.err.println(e.awsErrorDetails().errorMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteMessageFromQueue(String receiptHandle) {
        logger.info("Deleting message from Queue");
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(sqsUrl)
                .receiptHandle(receiptHandle)
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }

    private List<Message> pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsUrl)
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }

    private boolean validateMessage(Message message) {
        boolean valid = true;

        try {
            JSONObject json = new JSONObject(message.body());
            String stringMessage = json.getString("Message");
            json = new JSONObject(stringMessage);

            valid = json.has("event") && json.has("product");
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }
}

