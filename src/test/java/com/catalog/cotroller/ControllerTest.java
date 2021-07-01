package com.catalog.cotroller;

import com.catalog.controller.ProductCatalogController;
import com.catalog.controller.resource.ProductRequestResource;
import com.catalog.controller.resource.ProductResponseResource;
import com.catalog.entity.Product;
import com.catalog.service.ProductCatalogService;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.easymock.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

import static org.easymock.EasyMock.expect;

@RunWith(EasyMockRunner.class)
public class ControllerTest extends EasyMockSupport {

    @TestSubject
    private ProductCatalogController productCatalogController = new ProductCatalogController();

    @Mock
    private ProductCatalogService productCatalogService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    Mapper mapper;

    @Before
    public void setupAutowiredMocks() {
        ReflectionTestUtils.setField(productCatalogController, "productCatalogService", productCatalogService);
    }

    @After
    public void teardown() {
        verifyAll();
    }

    @Test
    public void getProductSuccessTest() {
        Product product = retrieveTestProductWithName("new");
        ProductResponseResource response = new ProductResponseResource();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());

        expect(productCatalogService.getProductById(product.getId())).andReturn(product).once();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        EasyMock.expectLastCall();
        expect(mapper.map(product, ProductResponseResource.class)).andReturn(response);
        replayAll();

        ProductResponseResource result = productCatalogController.getProduct("new", httpServletResponse);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), product.getId());
    }

    @Test
    public void addProductSuccessTest() {
        Product product = retrieveTestProductWithName("new");

        ProductRequestResource resource = new ProductRequestResource();
        resource.setPrice(product.getPrice());
        resource.setName(product.getName());
        resource.setQuantity(product.getQuantity());

        ProductResponseResource response = new ProductResponseResource();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());

        expect(productCatalogService.addProduct(resource)).andReturn(product).once();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        EasyMock.expectLastCall();
        expect(mapper.map(product, ProductResponseResource.class)).andReturn(response);
        replayAll();

        ProductResponseResource result = productCatalogController.addProduct(resource, httpServletResponse);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getId(), product.getId());
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

