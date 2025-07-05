package com.example.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("role_name")
    private String roleName; // 角色名
    private String description;
} 