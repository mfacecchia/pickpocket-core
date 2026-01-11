package com.feis.splitnings.common.exception;

import com.feis.splitnings.common.exception.model.Error;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private List<Error> errors = new ArrayList<>();

    public void addError(Error error) {
        this.errors.add(error);
    }
}
