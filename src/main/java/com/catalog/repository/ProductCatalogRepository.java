package com.catalog.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.catalog.entity.Product;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import com.catalog.service.ProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCatalogRepository {

    Logger logger = LoggerFactory.getLogger(ProductCatalogService.class);

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Product save(Product product) {
        try {
            dynamoDBMapper.save(product);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.AWS_DYNAMODB_ERROR);
        }

        return product;
    }

    public Product getProductById(String id) {
        try {
            return dynamoDBMapper.load(Product.class, id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_DYNAMODB_ERROR);
        }
    }

    public Product updateProductById(String id, Product product) {
        try {
            dynamoDBMapper.save(product,
                    new DynamoDBSaveExpression()
                            .withExpectedEntry("id", new ExpectedAttributeValue(
                                    new AttributeValue().withS(id)
                            ))
            );

            return product;
        } catch (ConditionalCheckFailedException e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_DYNAMODB_ERROR);
        }
    }

    public void deleteProductById(String id) {
        try {
            Product product = dynamoDBMapper.load(Product.class, id);

            dynamoDBMapper.delete(product);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_DYNAMODB_ERROR);
        }
    }

    public List<Product> listProducts() {
        try {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

            return dynamoDBMapper.scan(Product.class, scanExpression);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ErrorCode.AWS_DYNAMODB_ERROR);
        }
    }
}