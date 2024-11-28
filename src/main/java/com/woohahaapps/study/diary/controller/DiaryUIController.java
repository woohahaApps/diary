package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.Diary;
import com.woohahaapps.study.diary.service.DiaryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class DiaryUIController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryUIController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // Edit
    @GetMapping("/diary/edit/{id}")
    public String EditDiary(@PathVariable("id") Long id, Model model) {
        Diary diary = diaryService.GetDiary(id);
        model.addAttribute("diary", diary);

        return "diary/editdiary";
    }

    // Home
    @GetMapping("/")
    public String Home(Model model) {
        // 가장 최신 날짜의 일기를 위쪽으로 나열되게
        //List<Map<String, Object>> listDiaries = diaryService.GetAllDiaries();
        List<Diary> listDiaries = diaryService.GetAllDiaries();
        model.addAttribute("diaries", listDiaries);

        return "diary/home";
    }

    // Write
    @GetMapping("/write")
    public String Write(Model model) {
        return "diary/write";
    }
}
