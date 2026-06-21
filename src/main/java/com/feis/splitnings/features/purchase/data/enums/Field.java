package com.feis.splitnings.features.purchase.data.enums;

import lombok.Getter;

@Getter
public enum Field {
    id("id"),
    name("name"),
    description("description"),
    category("category"),
    splitId("splitId");

    private final String path;

    Field(String path) {
        this.path = path;
    }
}
