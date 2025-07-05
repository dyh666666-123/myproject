package com.example.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("SELECT * FROM role WHERE role_name = #{roleName}")
    Role findByName(String roleName);

    @Select("SELECT * FROM role WHERE role_name = #{roleName}")
    Role findByRoleName(String roleName);
} 