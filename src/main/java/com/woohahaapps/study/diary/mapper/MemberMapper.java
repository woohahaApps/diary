package com.woohahaapps.study.diary.mapper;

import com.woohahaapps.study.diary.domain.Diary;
import com.woohahaapps.study.diary.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    //public void CreateMember(String email, String password, String name);
    void CreateMember(Member member);

    Optional<Member> GetMember(String email);

    Optional<Member> GetMemberByUserId(Long uid);

    //public void UpdateMember(String email, String password, String name);
    void UpdateMember(Member member);

    void DeleteMember(String email);

    //List<Map<String, Object>> GetAllMembers();
    List<Member> GetAllMembers();
}
