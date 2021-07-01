package com.catalog.service;

import com.catalog.controller.resource.ProductRequestResource;
import com.catalog.entity.Product;
import com.catalog.exception.ApplicationException;
import com.catalog.exception.errors.ErrorCode;
import com.catalog.repository.ProductCatalogRepository;
import com.catalog.service.impl.ProductCatalogServiceImpl;
import org.easymock.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(EasyMockRunner.class)
public class ProductCatalogServiceTest extends EasyMockSupport {

    @TestSubject
    private ProductCatalogService productCatalogService = new ProductCatalogServiceImpl();

    @Mock
    private ProductCatalogRepository productCatalogRepository;

    @Mock
    private NotificationService notificationService;


    public void setup() {
        injectMocks(productCatalogRepository);
        injectMocks(notificationService);
    }


    @Test
    public void getProductByIdSuccessTest() {
        Product product = retrieveTestProductWithName("test");

        EasyMock.expect(productCatalogRepository.getProductById("test")).andReturn(product);
        EasyMock.replay(productCatalogRepository);

        Product expectedProduct = productCatalogService.getProductById(product.getName());

        Assert.assertNotNull(expectedProduct);
        Assert.assertEquals(expectedProduct, product);

        EasyMock.verify(productCatalogRepository);

    }

    @Test
    public void searchProductsSuccessTest() {
        List<Product> emptyProductList = new ArrayList<>();
        EasyMock.expect(productCatalogRepository.listProducts()).andReturn(emptyProductList);
        EasyMock.replay(productCatalogRepository);

        List<Product> expectedProducts = productCatalogService.listProducts();

        Assert.assertEquals(0, expectedProducts.size());

        EasyMock.verify(productCatalogRepository);
    }

    @Test
    public void updateProductSuccessTest() {
        Product product = retrieveTestProductWithName("test");
        ProductRequestResource resource = new ProductRequestResource();
        resource.setPrice(BigDecimal.valueOf(20));
        resource.setName("updated");
        resource.setQuantity(2);
        Product updatedProduct = retrieveTestProductWithName("test");
        updatedProduct.setQuantity(resource.getQuantity());
        updatedProduct.setPrice(resource.getPrice());
        updatedProduct.setName(resource.getName());

        EasyMock.expect(productCatalogRepository.getProductById("test")).andReturn(product);
        EasyMock.expect(productCatalogRepository.updateProductById("test", updatedProduct)).andReturn(updatedProduct);
        EasyMock.replay(productCatalogRepository);

        Product expectedProduct = productCatalogService.updateProduct(product.getId(), resource);

        Assert.assertNotNull(expectedProduct);
        Assert.assertEquals(expectedProduct, product);

        EasyMock.verify(productCatalogRepository);

    }

    @Test
    public void updateProductNotFoundTest() {
        Product product = retrieveTestProductWithName("test");
        ProductRequestResource resource = new ProductRequestResource();
        resource.setPrice(BigDecimal.valueOf(20));
        resource.setName("updated");
        resource.setQuantity(2);

        EasyMock.expect(productCatalogRepository.getProductById("test")).andReturn(null);
        EasyMock.replay(productCatalogRepository);

        try {
            productCatalogService.updateProduct(product.getId(), resource);
        } catch (ApplicationException e) {
            Assert.assertEquals(ErrorCode.CONTENT_NOT_FOUND, e.getErrorCode());
        }

        EasyMock.verify(productCatalogRepository);
    }

    @Test
    public void deleteProductNotFoundTest() {
        Product product = retrieveTestProductWithName("test");

        EasyMock.expect(productCatalogRepository.getProductById("test")).andReturn(null);
        EasyMock.replay(productCatalogRepository);

        try {
            productCatalogService.deleteProduct(product.getId());
        } catch (ApplicationException e) {
            Assert.assertEquals(ErrorCode.CONTENT_NOT_FOUND, e.getErrorCode());
        }

        EasyMock.verify(productCatalogRepository);

    }

    @Test
    public void deleteProductSuccessTest() {
        Product product = retrieveTestProductWithName("test");
        boolean success = true;

        EasyMock.expect(productCatalogRepository.getProductById("test")).andReturn(product);
        productCatalogRepository.deleteProductById("test");
        EasyMock.expectLastCall().andVoid();
        EasyMock.replay(productCatalogRepository);

        try {
            productCatalogService.deleteProduct(product.getId());
        } catch (ApplicationException e) {
            success = false;
        }

        Assert.assertTrue(success);

        EasyMock.verify(productCatalogRepository);

    }

    @Test
    public void addProductSuccessTest() {
        Product product = retrieveTestProductWithName("new");

        Product newProduct = new Product();
        newProduct.setName("new");
        newProduct.setPrice(BigDecimal.valueOf(100));
        newProduct.setQuantity(1);

        ProductRequestResource resource = new ProductRequestResource();
        resource.setPrice(BigDecimal.valueOf(100));
        resource.setName("new");
        resource.setQuantity(1);

        EasyMock.expect(productCatalogRepository.save(newProduct)).andReturn(newProduct);
        EasyMock.replay(productCatalogRepository);

        Product expectedProduct = productCatalogService.addProduct(resource);

        Assert.assertNotNull(expectedProduct);

        EasyMock.verify(productCatalogRepository);

    }

    private Product retrieveTestProductWithName(String name) {

        Product product = new Product();
        product.setId(name);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(100));
        product.setQuantity(1);

        return product;
    }
}
