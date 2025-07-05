package com.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.library.entity.Role;
import com.example.library.entity.User;
import com.example.library.entity.UserRole;
import com.example.library.mapper.RoleMapper;
import com.example.library.mapper.UserMapper;
import com.example.library.service.UserRoleService;
import com.example.library.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public boolean saveUser(User user) {
        // 1. 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. 保存用户基本信息
        boolean userSaved = this.save(user);
        if (!userSaved) {
            return false;
        }
        
        // 3. 查找 'USER' 角色并分配
        Role userRoleEntity = roleMapper.findByRoleName("USER");
        if (userRoleEntity != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(userRoleEntity.getId());
            userRoleService.save(userRole);
        } else {
             // For a real app, you'd want to log this as a warning.
             // We'll throw an exception to make it obvious if a base role is missing.
             throw new IllegalStateException("Default role 'USER' not found in database.");
        }

        // 4. 查找 'ADMIN' 角色并分配 (这是解决403错误的关键)
        Role adminRoleEntity = roleMapper.findByRoleName("ADMIN");
        if (adminRoleEntity != null) {
            UserRole adminRole = new UserRole();
            adminRole.setUserId(user.getId());
            adminRole.setRoleId(adminRoleEntity.getId());
            userRoleService.save(adminRole);
        } else {
            // It's critical to know if the ADMIN role is missing during development.
            throw new IllegalStateException("CRITICAL: Role 'ADMIN' not found in database. Cannot create admin user.");
        }
        
        return true;
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        // 1. 删除用户与角色的关联关系
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        // 2. 删除用户本身
        return this.removeById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<String> findRolesByUsername(String username) {
        return userMapper.findRolesByUsername(username);
    }

    @Transactional
    @Override
    public boolean grantAdminRole(String username) {
        User user = findByUsername(username);
        if (user == null) {
            log.warn("Cannot grant admin role: User '{}' not found.", username);
            return false;
        }

        Role adminRole = roleMapper.findByRoleName("ADMIN");
        if (adminRole == null) {
            log.error("CRITICAL: Role 'ADMIN' not found in database. Cannot grant role.");
            throw new IllegalStateException("CRITICAL: Role 'ADMIN' not found in database.");
        }

        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId()).eq(UserRole::getRoleId, adminRole.getId());
        boolean alreadyAdmin = userRoleService.count(queryWrapper) > 0;

        if (alreadyAdmin) {
            log.info("User '{}' already has ADMIN role.", username);
            return true;
        }

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(adminRole.getId());
        log.info("Assigning ADMIN role to user '{}'.", username);
        return userRoleService.save(userRole);
    }
} 