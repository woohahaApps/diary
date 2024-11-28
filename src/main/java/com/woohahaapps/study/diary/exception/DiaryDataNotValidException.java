package com.woohahaapps.study.diary.exception;

public class DiaryDataNotValidException extends RuntimeException {
    public DiaryDataNotValidException() {}
    public DiaryDataNotValidException(String message) {
        super(message);
    }
}
