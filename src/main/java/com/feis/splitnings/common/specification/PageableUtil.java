package com.feis.splitnings.common.specification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
    public static Pageable buildPageable(Integer page, Integer size, String sortField, String direction) {
        if(page==null || size==null || sortField==null){
            return null;
        }

        Sort sort = null;
        if (direction.equalsIgnoreCase("DESC")) {
            sort = Sort.by(sortField).descending();
        } else if (direction.equalsIgnoreCase("ASC")) {
            sort = Sort.by(sortField).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        return pageable;
    }
}
