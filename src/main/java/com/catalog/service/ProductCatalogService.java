package com.catalog.service;

import com.catalog.controller.resource.ProductRequestResource;
import com.catalog.entity.Product;
import java.util.List;

public interface ProductCatalogService {

    Product getProductById(String id);

    List<Product> listProducts();

    Product addProduct(ProductRequestResource product);

    void deleteProduct(String id);

    Product updateProduct(String id, ProductRequestResource product);
}
