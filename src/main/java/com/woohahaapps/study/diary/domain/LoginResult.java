package com.woohahaapps.study.diary.domain;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class LoginResult {
    String body;// token
    Integer errorCode;// errorCode (200(OK), 400(BAD_REQUEST)
    String errorMessage;
}
