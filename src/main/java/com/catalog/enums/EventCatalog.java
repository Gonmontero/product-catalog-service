package com.catalog.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum EventCatalog {

    NEW_PRODUCT("new_product"),
    REMOVED_PRODUCT("removed_product"),
    UPDATED_PRODUCT("updated_product");

    private final String name;

    EventCatalog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EventCatalog fromName(String name) {
        if (!StringUtils.isEmpty(name)) {
            for (EventCatalog eventEnum : values()) {
                if (eventEnum.getName().equalsIgnoreCase(name)) {
                    return eventEnum;
                }
            }
        }
        return null;
    }

    public static List<EventCatalog> listEvents() {
        return Arrays.asList(EventCatalog.values());
    }
}