package com.catalog.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.catalog.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCatalogRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Product save(Product product) {
        dynamoDBMapper.save(product);
        return product;
    }

    public Product getProductById(String id) {
        return dynamoDBMapper.load(Product.class, id);
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
            System.err.println("Error while Updating the product");
            throw new RuntimeException(e);
        }
    }

    public void deleteProductById(String id) {
        Product product = dynamoDBMapper.load(Product.class, id);
        dynamoDBMapper.delete(product);
    }

    public List<Product> listProducts() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();

        return dynamoDBMapper.scan(Product.class, scanExpression);
    }
}