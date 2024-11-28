package com.woohahaapps.study.diary.mapper;

import com.woohahaapps.study.diary.domain.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface DiaryMapper {
    //public void CreateDiary(@Param("date") String date, @Param("content") String content, @Param("email") String email);
    void CreateDiary(Diary diary);

    //public Diary GetDiary(Integer id);
    Optional<Diary> GetDiary(Long id);

    //public void UpdateDiary(Integer id, String diary_date, String diary_content);
    void UpdateDiary(Diary diary);

    void DeleteDiary(Long id);

//    List<Map<String, Object>> GetAllDiaries(@Param("email") String email);
    List<Diary> GetAllDiaries(@Param("email") String email);
}
