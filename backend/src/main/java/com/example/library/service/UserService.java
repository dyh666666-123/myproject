package com.example.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.library.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    boolean saveUser(User user);

    boolean deleteUser(Long userId);

    User findByUsername(String username);

    List<String> findRolesByUsername(String username);

    boolean grantAdminRole(String username);
    // 可扩展自定义方法
} 