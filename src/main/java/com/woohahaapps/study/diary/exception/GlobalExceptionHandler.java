package com.woohahaapps.study.diary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiaryException.class)
    protected ResponseEntity<ErrorResponse> handleDiaryException(DiaryException e) {
        final ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DiaryDataNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleDiaryDataNotValidException(DiaryDataNotValidException e) {
        final ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DiaryDataNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleDiaryDataNotFoundException(DiaryDataNotFoundException e) {
        final ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DiaryUserNotMatchException.class)
    protected ResponseEntity<ErrorResponse> handleDiaryUserNotMatchException(DiaryUserNotMatchException e) {
        final ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
