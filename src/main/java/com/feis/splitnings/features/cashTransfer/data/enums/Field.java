package com.feis.splitnings.features.cashTransfer.data.enums;

import lombok.Getter;

@Getter
public enum Field {
    description("description"),
    fromSplitId("fromSplitId"),
    toSplitId("toSplitId"),
    accountId("accountId");

    private final String path;

    Field(String path) {
        this.path = path;
    }
}
