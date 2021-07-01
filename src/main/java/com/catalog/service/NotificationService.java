package com.catalog.service;

import com.catalog.entity.Product;
import com.catalog.enums.EventCatalog;

public interface NotificationService {

    void notifyDownstreamServices(EventCatalog eventName, Product product);

}
