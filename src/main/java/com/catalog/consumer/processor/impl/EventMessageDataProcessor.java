package com.catalog.consumer.processor.impl;

import com.catalog.consumer.SQSConsumer;
import com.catalog.consumer.processor.DataProcessor;
import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class EventMessageDataProcessor implements DataProcessor<Product, EventCatalog> {

    @Value("${app.notification.supplychain.url}")
    private String supplyChainUrl;

    Logger logger = LoggerFactory.getLogger(SQSConsumer.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void processData(Product product, EventCatalog event) {
        switch (event) {
            case NEW_PRODUCT:
            case UPDATED_PRODUCT:
                sendPostRequestToSupplyChain(product);
                break;
            case REMOVED_PRODUCT:
                sendDeleteRequestToSupplyChain(product);
                break;
        }
    }

    private void sendPostRequestToSupplyChain(Product product) {
        HttpEntity<Product> request = new HttpEntity<>(product);
        try {
            logger.info("Posting product id {} to chain-supply", product.getId());

            restTemplate.postForObject(supplyChainUrl, request, String.class);
        } catch (RestClientException ex) {
            throw new ApplicationException(ErrorCode.SUPPLY_CHAIN_UNEXPECTED_ERROR);
        }
    }

    private void sendDeleteRequestToSupplyChain(Product product) {
        HttpEntity<Product> request = new HttpEntity<>(product);

        try {
            logger.info("Requesting delete of product id {} from chain-supply", product.getId());
            String path = supplyChainUrl + "/" + product.getId();

            restTemplate.delete(path, request, Product.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.info(e.getMessage());
        } catch (RestClientException ex) {
            throw new ApplicationException(ErrorCode.SUPPLY_CHAIN_UNEXPECTED_ERROR);
        }
    }
}
