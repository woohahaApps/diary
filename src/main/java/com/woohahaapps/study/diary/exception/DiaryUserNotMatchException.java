package com.woohahaapps.study.diary.exception;

public class DiaryUserNotMatchException extends RuntimeException {
    public DiaryUserNotMatchException() {}
    public DiaryUserNotMatchException(String message) {
        super(message);
    }
}
