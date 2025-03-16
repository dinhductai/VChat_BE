package com.website.loveconnect.exception;

import com.website.loveconnect.exception.exceptionmodel.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleAllExceptions(Exception ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),"An error occurred :"+ex.getMessage(),
                "check this uri :"+request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetail> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetail,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableExceptionHandler.class)
    public ResponseEntity<ErrorDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableExceptionHandler ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetail,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        ErrorDetail errorDetail = new ErrorDetail(LocalDateTime.now(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetail,HttpStatus.NOT_FOUND);
    }

    //bat loi validation khi du lieu request body khong hop le
    @ExceptionHandler(MethodArgumentNotValidException.class)  // .bind
    public ResponseEntity<Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(),error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
