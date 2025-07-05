package com.example.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT r.role_name FROM role r " +
            "LEFT JOIN user_role ur ON r.id = ur.role_id " +
            "LEFT JOIN user u ON u.id = ur.user_id " +
            "WHERE u.username = #{username}")
    List<String> findRolesByUsername(String username);
}