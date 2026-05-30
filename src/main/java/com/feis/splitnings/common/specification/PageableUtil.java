package com.feis.splitnings.common.specification;

import com.feis.splitnings.common.exception.errors.Error;
import com.feis.splitnings.common.exception.errors.ValidationError;
import com.feis.splitnings.common.exception.ValidationException;
import com.feis.splitnings.common.exception.enums.InternalErrorCode;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

    public static Pageable buildPageable(Integer page, Integer size, String sortField, String direction) {
        if(page==null || size==null || sortField==null){
            return null;
        }

        validatePageable(page, size);

        Sort sort = null;
        if (direction.equalsIgnoreCase("DESC")) {
            sort = Sort.by(sortField).descending();
        } else if (direction.equalsIgnoreCase("ASC")) {
            sort = Sort.by(sortField).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return pageable;
    }

    private static void validatePageable(Integer page, Integer size) {
        List<Error> errors = new ArrayList<>();

        if (page < 0) {
            errors.add(new ValidationError("page", InternalErrorCode.PARAMETER_INVALID, "Page index must be higher or equal than 0"));
        }
        if (size <= 0) {
            errors.add(new ValidationError("size", InternalErrorCode.PARAMETER_INVALID, "Page size must be higher or equal than 1"));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

    }
}
