package com.catalog.service.impl;

import com.catalog.controller.resource.ProductRequestResource;
import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import com.catalog.repository.ProductCatalogRepository;
import com.catalog.service.NotificationService;
import com.catalog.service.ProductCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {

    Logger logger = LoggerFactory.getLogger(ProductCatalogService.class);

    @Autowired
    private ProductCatalogRepository productCatalogRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Product getProductById(String id) {
        //fetch product from DB, if it is not found, then let the controller return content not found.
        return productCatalogRepository.getProductById(id);
    }

    @Override
    public List<Product> listProducts() {
        List<Product> products = productCatalogRepository.listProducts();

        if (CollectionUtils.isEmpty(products)) {
            logger.info("The list of products is empty");
        }

        return products;
    }

    @Override
    public Product addProduct(ProductRequestResource productRequest) {
        logger.debug("Saving into DB");

        Product product = Product.Builder.Builder()
                .withName(productRequest.getName())
                .withPrice(productRequest.getPrice())
                .withQuantity(productRequest.getQuantity())
                .build();

        productCatalogRepository.save(product);

        try {
            notificationService.notifyDownstreamServices(EventCatalog.NEW_PRODUCT, product);
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
        }

        return product;
    }

    @Override
    public void deleteProduct(String id) {
        Product product = this.getProductById(id);

        if (product == null) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND);
        }

        productCatalogRepository.deleteProductById(product.getId());

        try {
            notificationService.notifyDownstreamServices(EventCatalog.REMOVED_PRODUCT, product);
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Product updateProduct(String id, ProductRequestResource requestResource) {

        Product product = this.getProductById(id);

        if (product == null) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND);
        }

        //upsert
        if (requestResource.getQuantity() != null) product.setQuantity(requestResource.getQuantity());
        if (requestResource.getName() != null) product.setName(requestResource.getName());
        if (requestResource.getPrice() != null) product.setPrice(requestResource.getPrice());


        productCatalogRepository.updateProductById(product.getId(), product);

        try {
            notificationService.notifyDownstreamServices(EventCatalog.UPDATED_PRODUCT, product);
        } catch(ApplicationException e) {
            System.out.println(e.getMessage());
        }

        return product;
    }
}
