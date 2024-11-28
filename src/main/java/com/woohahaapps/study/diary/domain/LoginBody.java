package com.woohahaapps.study.diary.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginBody {
    String email;
    String password;
}
