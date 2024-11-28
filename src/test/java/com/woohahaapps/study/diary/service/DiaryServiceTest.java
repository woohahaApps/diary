package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.Diary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;

@SpringBootTest
//@Transactional
class DiaryServiceTest {

    private final DiaryService diaryService;

    @Autowired
    DiaryServiceTest(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @Test
    @WithMockUser(username="woohaha@gmail.com")
    void createDiary() {
        //diaryService.CreateDiary("2024-01-01", "diary create test222");
        Diary diary = Diary.builder()
                .diary_date(LocalDate.parse("2024-01-01"))
                .diary_content("diary create test2222")
                .build();
        diaryService.CreateDiary(diary);
    }

    @Test
    void getAllDiaries() {
//        List<Map<String, Object>> listDiary = diaryService.GetAllDiaries();
//        for (Map<String, Object> diary : listDiary) {
//            System.out.println("id=" + diary.get("id") + ", date=" + diary.get("diary_date") + ", content=" + diary.get("diary_content"));
//        }
        List<Diary> listDiary = diaryService.GetAllDiaries();
        for (Diary diary : listDiary) {
            System.out.println(diary);
        }
    }
}