package com.training.trackMailApi.service.impl;

import com.training.trackMailApi.service.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public List<String> generateErrorMessage(BindingResult bindingResult) {
        return Optional.of(bindingResult)
                .filter(BindingResult::hasErrors)
                .map(this::getErrors)
                .orElseGet(ArrayList::new);
    }

    private List<String> getErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();

        return errors.stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
