package com.training.trackMailApi.service;

import org.springframework.validation.BindingResult;

import java.util.List;

public interface ValidationService {

    List<String> generateErrorMessage(BindingResult bindingResult);
}
