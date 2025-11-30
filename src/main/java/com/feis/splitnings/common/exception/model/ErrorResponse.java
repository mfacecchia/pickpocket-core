package com.feis.splitnings.common.exception.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private String path;
    private int status;
    private String error;
    private List<Error> errors = new ArrayList<>();
}
