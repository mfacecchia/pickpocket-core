package com.feis.splitnings.common.specification;

public enum SearchCriteriaType {
    EQUAL_TO("="),
    NOT_EQUAL_TO("!"),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    LIKE("like"),
    BETWEEN("between"),
    IN("in"),
    NOT_IN("notIn"),
    DATE_FROM(">="),
    DATE_TO("<=");

    public final String label;

    private SearchCriteriaType(String label) {
        this.label = label;
    }
}
