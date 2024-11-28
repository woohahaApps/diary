package com.woohahaapps.study.diary.mapper;

import com.woohahaapps.study.diary.domain.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UsersMapper {
    Integer CreateUsers(Users users);
    Optional<Users> GetUsers(String provider_user_id, String provider);
}
