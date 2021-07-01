package com.catalog.service;

import com.catalog.Publisher.SNSPublisher;
import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;
import com.catalog.service.impl.SNSNotificationService;
import org.easymock.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

@RunWith(EasyMockRunner.class)
public class SNSNotificationServiceTest extends EasyMockSupport {

    @TestSubject
    private SNSNotificationService notificationService = new SNSNotificationService();

    @Mock
    private SNSPublisher snsPublisher;

    public void setup() {
        injectMocks(snsPublisher);
    }

    @Test
    public void notifyDownstreamServicesTest() {
        Product product = retrieveTestProductWithName("Test");
        EventCatalog event = EventCatalog.NEW_PRODUCT;

        snsPublisher.publishMessageToTopic(null, "{\"product\":{\"id\":\"Test\",\"name\":\"Test\",\"price\":100,\"quantity\":1},\"event\":\"new_product\"}");
        EasyMock.replay(snsPublisher);

        notificationService.notifyDownstreamServices(event, product);

        EasyMock.verify(snsPublisher);
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
