package com.feis.splitnings.common.specification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
    private String key;
    private SearchCriteriaType operation;
    private Object value;
    private Object value2;
    private boolean isOrPredicate;

    public SearchCriteria(String key, SearchCriteriaType searchCriteriaType, Object value, boolean isOrPredicate) {
        this.key = key;
        this.operation = searchCriteriaType;
        this.value = value;
        this.value2 = null;
        this.isOrPredicate = isOrPredicate;
    }


    public SearchCriteria(String key, SearchCriteriaType searchCriteriaType, Object value, Object value2, boolean isOrPredicate) {
        this.key = key;
        this.operation = searchCriteriaType;
        this.value = value;
        this.value2 = value2;
        this.isOrPredicate = isOrPredicate;
    }
}
