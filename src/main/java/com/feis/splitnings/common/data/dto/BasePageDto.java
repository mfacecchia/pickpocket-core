package com.feis.splitnings.common.data.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageDto<DTO> {
    public List<DTO> content = new ArrayList<>();
    private int pageElements;
    private long totalCount;
}
