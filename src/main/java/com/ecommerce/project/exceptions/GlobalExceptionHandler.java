package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.APIResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        Map<String, String> response = new HashMap<>();
        e.getBindingResult()
                .getFieldErrors()
                .forEach(err -> {
                    String fieldName = ((FieldError)err).getField();
                    String message = err.getDefaultMessage();
                    response.put(fieldName, message);
                });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> ResourceNotFoundExceptionHandler(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> APIExceptionHandler(APIException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
