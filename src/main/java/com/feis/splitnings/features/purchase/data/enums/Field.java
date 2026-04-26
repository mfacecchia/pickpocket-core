package com.feis.splitnings.features.purchase.data.enums;

import lombok.Getter;

@Getter
public enum Field {
    id("id"),
    name("name"),
    description("description"),
    splitId("splitId"),
    deleted("deleted");

    private final String path;

    Field(String path) {
        this.path = path;
    }
}
