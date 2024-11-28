package com.woohahaapps.study.diary.controller;

import com.woohahaapps.study.diary.domain.Diary;
import com.woohahaapps.study.diary.exception.DiaryException;
import com.woohahaapps.study.diary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
//@SecurityRequirement(name = "basicAuth")
public class DiaryController {
    private final DiaryService diaryService;

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // Create
//    @PostMapping("/diary")
//    public RedirectView CreateDiary(String date, String content) {
//        System.out.println("date=" + date + ",content=" + content);
//        diaryService.CreateDiary(date, content);
//        return new RedirectView("/");
//    }
//    @PostMapping("/diary")
//    public void CreateDiary(@RequestBody Diary diary) {
//        System.out.println(diary);
//        diaryService.CreateDiary(diary);
//    }



    @PostMapping("/diary")
    public ResponseEntity<Objects> CreateDiary(@RequestBody Diary diary) {
        System.out.println(diary);
        diaryService.CreateDiary(diary);
        //return ResponseEntity.ok().build();
        return ResponseEntity.created(URI.create("/")).build();
    }




//    public ResponseEntity<Objects> CreateDiary(@RequestBody Diary diary) {
//        //System.out.println("date=" + date + ",content=" + content);
//        System.out.println(diary);
//        //diaryService.CreateDiary(date, content);
//        diaryService.CreateDiary(diary);
//        return ResponseEntity.ok().build();
//    }

    // Read
//    @GetMapping("/diary/{id}")
//    public Diary GetDiary(@PathVariable("id") Integer id) {
//        return diaryService.GetDiary(id);
//    }
//    @GetMapping("/diary/{id}")
//    public ResponseEntity<Diary> GetDiary(@PathVariable("id") Integer id) {
//        //return diaryService.GetDiary(id);
//        try {
//            Diary diary = diaryService.GetDiary(id);
//            return ResponseEntity.ok().body(diary);
//        } catch (DiaryException ex) {
//            return ResponseEntity.badRequest().build();
//        } catch (Exception ex) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//    @GetMapping("/diary/{id}")
//    public ResponseEntity<Object> GetDiary(@PathVariable("id") Integer id) {
//        //return diaryService.GetDiary(id);
//        try {
//            Diary diary = diaryService.GetDiary(id);
//            return ResponseEntity.ok().body(diary);
//        } catch (DiaryException ex) {
//            //return ResponseEntity.badRequest().build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//        } catch (Exception ex) {
//            //return ResponseEntity.internalServerError().build();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//    }
    @GetMapping("/diary/{id}")
    public ResponseEntity<Diary> GetDiary(@PathVariable("id") Long id) {
        //return diaryService.GetDiary(id);
        Diary diary = diaryService.GetDiary(id);
        return ResponseEntity.ok().body(diary);
    }

    // Update
//    @PutMapping(value = "/diary/{id}")
//    public RedirectView UpdateDiary(@PathVariable("id") Integer id, String diary_date, String diary_content) {
//        System.out.println("id=" + id + ", date=" + diary_date + ", content=" + diary_content);
//        diaryService.UpdateDiary(id, diary_date, diary_content);
//        return new RedirectView("/");
//    }

//    @PutMapping(value = "/diary/{id}")
//    public void UpdateDiary(@PathVariable("id") Integer id, String diary_date, String diary_content) {
//        System.out.println("id=" + id + ", date=" + diary_date + ", content=" + diary_content);
//        diaryService.UpdateDiary(id, diary_date, diary_content);
//    }

//    @PutMapping(value = "/diary/{id}")
//    public void UpdateDiary(@PathVariable("id") Integer id, @RequestBody Map<String, Object> map) {
//        System.out.println("id=" + id);
//        System.out.println(map);
//        diaryService.UpdateDiary(id, map.get("diary_date").toString(), map.get("diary_content").toString());
//    }

    @PutMapping(value = "/diary/{id}")
    public void UpdateDiary(@PathVariable("id") Long id, @RequestBody Diary diary) {
        System.out.println("id=" + id);
        System.out.println(diary);
        diaryService.UpdateDiary(id, diary);
    }

    // Delete
    @DeleteMapping("/diary/{id}")
    public void DeleteDiary(@PathVariable("id") Long id) {
        diaryService.DeleteDiary(id);
    }
    // all Diary
    @GetMapping("/diary/all")
//    public List<Map<String, Object>> GetAllDiaries() {
//        return diaryService.GetAllDiaries();
//    }
    public List<Diary> GetAllDiaries() {
        return diaryService.GetAllDiaries();
    }
}
