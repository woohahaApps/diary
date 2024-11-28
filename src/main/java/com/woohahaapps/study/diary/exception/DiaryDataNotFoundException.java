package com.woohahaapps.study.diary.exception;

public class DiaryDataNotFoundException extends RuntimeException {
    public DiaryDataNotFoundException() {}
    public DiaryDataNotFoundException(String message) {
        super(message);
    }
}
