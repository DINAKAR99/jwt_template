package cgg.blogapp.blogapp.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cgg.blogapp.blogapp.payload.ApiResponse;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleresNotFound(ResourceNotFoundException r1) {
        return new ResponseEntity<ApiResponse>(new ApiResponse(r1.getMessage(), false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlerRefreshTokenExpired(RefreshTokenExpired rf) {

        return new ResponseEntity<>("refresh token expired", HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerMethodArgNotValid(MethodArgumentNotValidException m1) {
        Map<String, String> response = new HashMap<>();

        BindingResult bindingResult = m1.getBindingResult();

        bindingResult.getAllErrors().forEach(e -> {

            String fielderr = ((FieldError) e).getField();
            e.getDefaultMessage();

            response.put(fielderr, fielderr);
        }

        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

}
