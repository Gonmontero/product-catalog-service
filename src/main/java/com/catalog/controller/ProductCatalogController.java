package com.catalog.controller;

import com.catalog.controller.handler.ApplicationExceptionHandler;
import com.catalog.controller.resource.ProductRequestResource;
import com.catalog.controller.resource.ProductResponseResource;
import com.catalog.entity.Product;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import com.catalog.service.ProductCatalogService;
import com.github.dozermapper.core.Mapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "Product")
@RequestMapping("/supply-chain")
public class ProductCatalogController extends ApplicationExceptionHandler {

    @Autowired
    ProductCatalogService productCatalogService;

    @Autowired
    Mapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductResponseResource> listProducts(HttpServletResponse httpResponse) {
        List<Product> products = productCatalogService.listProducts();
        List<ProductResponseResource> response = new ArrayList<>();

        if (products != null) {
            products.forEach(p -> response.add(mapper.map(p, ProductResponseResource.class)));
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }

        return response;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponseResource getProduct(@PathVariable(value = "id") String id, HttpServletResponse httpResponse) {
        Product response = productCatalogService.getProductById(id);

        if (response == null) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND);
        }
        httpResponse.setStatus(HttpServletResponse.SC_OK);

        return mapper.map(response, ProductResponseResource.class);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ProductResponseResource addProduct(@RequestBody ProductRequestResource product, HttpServletResponse httpResponse) {
        Product response = productCatalogService.addProduct(product);

        httpResponse.setStatus(HttpServletResponse.SC_OK);

        return mapper.map(response, ProductResponseResource.class);
    }

    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponseResource updateProduct(@PathVariable(value = "id") String id, @RequestBody ProductRequestResource product, HttpServletResponse httpResponse) {
        Product response = productCatalogService.updateProduct(id, product);

        httpResponse.setStatus(HttpServletResponse.SC_OK);

        return mapper.map(response, ProductResponseResource.class);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(value = "id") String id) {
        productCatalogService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
