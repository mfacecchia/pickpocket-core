package com.feis.splitnings.common.specification;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonSpecificationBuilder<ENTITY> {
    private List<CommonSpecification<ENTITY>> specificationList = new ArrayList<>();

    public CommonSpecificationBuilder<ENTITY> with(CommonSpecification<ENTITY> specification) {
        specificationList.add(specification);
        return this;
    }

    public CommonSpecificationBuilder<ENTITY> withFieldEqual(String fieldName, Object value) {
        SearchCriteria criteria = new SearchCriteria(fieldName, SearchCriteriaType.EQUAL_TO, value, false);
        specificationList.add(new CommonSpecification<>(criteria));

        return this;
    }
    public Specification<ENTITY> build() {

        if (specificationList.isEmpty()) {
            return null;
        }

        Specification<ENTITY> result = null;

        for (CommonSpecification<ENTITY> spec : specificationList) {
            if (spec.getCriteria().getValue() == null) {
                continue;
            }
            if (result == null) {
                result = spec;
                continue;
            }
            result = spec.getCriteria().isOrPredicate() ? result.or(spec) : result.and(spec);
        }

        return result;
    }


    /**
     * @param specification
     * @return CommonSpecificationBuilder
     * @description add specification by passing CommonSpecification object
     */
    public CommonSpecificationBuilder<ENTITY> where(CommonSpecification<ENTITY> specification) {
        specificationList.add(specification);
        return this;
    }

    /**
     * @param criteria
     * @return CommonSpecificationBuilder
     * @description add specification by passing SearchCriteria object
     */
    public CommonSpecificationBuilder<ENTITY> where(SearchCriteria criteria) {
        specificationList.add(new CommonSpecification<>(criteria));
        return this;
    }

    /**
     * @param key
     * @param searchCriteriaType
     * @param value
     * @param isOrPredicate
     * @return CommonSpecificationBuilder
     * @description add specification by passing key, SearchCriteriaType, value and isOrPredicate
     */
    public CommonSpecificationBuilder<ENTITY> where(String key, SearchCriteriaType searchCriteriaType, Object value, boolean isOrPredicate) {
        specificationList.add(new CommonSpecification<>(new SearchCriteria(key, searchCriteriaType, value, isOrPredicate)));
        return this;
    }

    //-----------------------------------------------------------------------------------------------------

    // Below are the overloaded methods for the most common search criteria types

    public CommonSpecificationBuilder<ENTITY> whereEqualTo(String key, Object value, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.EQUAL_TO, value, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> like(String key, Object value, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.LIKE, value, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> notEqualTo(String key, Object value, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.NOT_EQUAL_TO, value, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> greaterThan(String key, Object value, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.GREATER_THAN, value, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> greaterThanOrEqualTo(String key, Object value, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.GREATER_THAN_OR_EQUAL_TO, value, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> lessThan(String key, Object value) {
        return where(key, SearchCriteriaType.LESS_THAN, value, false);
    }

    public CommonSpecificationBuilder<ENTITY> lessThanOrEqualTo(String key, Object value) {
        return where(key, SearchCriteriaType.LESS_THAN_OR_EQUAL_TO, value, false);
    }

    public CommonSpecificationBuilder<ENTITY> between(String key, Integer minValue, Integer maxValue, boolean isOrPredicate) {
        SearchCriteria searchCriteria = new SearchCriteria(key, SearchCriteriaType.BETWEEN, minValue, maxValue, isOrPredicate);
        return where(searchCriteria);
    }

    public CommonSpecificationBuilder<ENTITY> in(String key, List<?> values, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.IN, values, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> notIn(String key, List<?> values, boolean isOrPredicate) {
        return where(key, SearchCriteriaType.NOT_IN, values, isOrPredicate);
    }

    public CommonSpecificationBuilder<ENTITY> beforeDate(String key, LocalDateTime value) {
        return where(key, SearchCriteriaType.DATE_TO, value, false);
    }

    public CommonSpecificationBuilder<ENTITY> afterDate(String key, LocalDateTime value) {
        return where(key, SearchCriteriaType.DATE_FROM, value, false);
    }
}
