package com.woohahaapps.study.diary.domain;

import lombok.Builder;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Data
@Builder
public class Diary {
    private Long id;
    private LocalDate diary_date;
    private String diary_content;
    private String email;
}
