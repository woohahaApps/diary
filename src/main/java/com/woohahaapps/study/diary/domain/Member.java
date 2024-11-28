package com.woohahaapps.study.diary.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Member {
    @NotBlank(message = "이메일 주소는 필수값입니다.")
    private String email;
//    @NotBlank(message = "패스워드는 필수값입니다.")
    private String password;
    @NotBlank(message = "이름은 필수값입니다.")
    private String name;
    private String role;
    private String provider;// 외부인증정보 제공자
    private Long uid;
}
