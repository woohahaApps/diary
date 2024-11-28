package com.woohahaapps.study.diary.service;

import com.woohahaapps.study.diary.domain.Diary;
import com.woohahaapps.study.diary.exception.DiaryDataNotFoundException;
import com.woohahaapps.study.diary.exception.DiaryDataNotValidException;
import com.woohahaapps.study.diary.exception.DiaryException;
import com.woohahaapps.study.diary.exception.DiaryUserNotMatchException;
import com.woohahaapps.study.diary.mapper.DiaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DiaryService {
    private final DiaryMapper diaryMapper;
    private final JavaMailSender mailSender;

    @Autowired
    public DiaryService(DiaryMapper diaryMapper, JavaMailSender mailSender) {
        this.diaryMapper = diaryMapper;
        this.mailSender = mailSender;
    }

    //public void CreateDiary(String date, String content) throws DiaryException {
    public void CreateDiary(Diary diary) throws DiaryException {
        //System.out.println("Service:date=" + date + ",content=" + content);
        System.out.println(diary);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);

        //if (email.isBlank() || date.isBlank() || content.isBlank()) {
        if (email.isBlank() || diary.getDiary_date().toString().isBlank() || diary.getDiary_content().isBlank()) {
            throw new DiaryDataNotValidException("일기정보가 부족합니다:"
                    + (email.isBlank() ? " email" : "")
                    + (diary.getDiary_date().toString().isBlank() ? " date" : "")
                    + (diary.getDiary_content().isBlank() ? " content" : "")
            );
        }
        diary.setEmail(email);
        //diaryMapper.CreateDiary(date, content, email);
        diaryMapper.CreateDiary(diary);

        // Mail 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[diary] " + diary.getDiary_date().toString());
        message.setText(diary.getDiary_content());
        mailSender.send(message);
    }

    public Diary GetDiary(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        //Diary diary = diaryMapper.GetDiary(id);
        Optional<Diary> diary = diaryMapper.GetDiary(id);
        if (diary.isEmpty()) {
            throw new DiaryDataNotFoundException("일기데이터가 존재하지 않습니다.");
        }
        if (!diary.get().getEmail().equals(email)) {
            throw new DiaryUserNotMatchException("작성자가 아닙니다.");
        }
        return diary.get();
    }

//    public void UpdateDiary(Integer id, String diary_date, String diary_content) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Diary diary = diaryMapper.GetDiary(id);
//        if (!diary.getEmail().equals(email)) {
//            throw new DiaryUserNotMatchException("작성자가 아닙니다.");
//        }
//        diaryMapper.UpdateDiary(id, diary_date, diary_content);
//    }

    public void UpdateDiary(Long id, Diary diaryUpdate) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Diary> diary = diaryMapper.GetDiary(id);
        if (diary.isEmpty()) {
            throw new DiaryDataNotFoundException("일기데이터가 존재하지 않습니다.");
        }
        if (!diary.get().getEmail().equals(email)) {
            throw new DiaryUserNotMatchException("작성자가 아닙니다.");
        }
        diaryUpdate.setId(id);
        diaryUpdate.setEmail(diary.get().getEmail());
        diaryMapper.UpdateDiary(diaryUpdate);
    }

    public void DeleteDiary(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Diary> diary = diaryMapper.GetDiary(id);
        if (diary.isEmpty()) {
            throw new DiaryDataNotFoundException("일기데이터가 존재하지 않습니다.");
        }
        if (!diary.get().getEmail().equals(email)) {
            throw new DiaryUserNotMatchException("작성자가 아닙니다.");
        }
        diaryMapper.DeleteDiary(id);
    }

//    public List<Map<String, Object>> GetAllDiaries() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return diaryMapper.GetAllDiaries(email);
//    }
    public List<Diary> GetAllDiaries() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return diaryMapper.GetAllDiaries(email);
    }
}
