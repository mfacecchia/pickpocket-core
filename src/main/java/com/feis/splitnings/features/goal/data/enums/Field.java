package com.feis.splitnings.features.goal.data.enums;

import lombok.Getter;

@Getter
public enum Field {
    id("id"),
    name("name"),
    completed("completed"),
    splitId("splitId"),
    accountId("split.accountId");

    private final String path;

    Field(String path) {
        this.path = path;
    }
}
