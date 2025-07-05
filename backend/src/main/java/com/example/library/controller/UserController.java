package com.example.library.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.entity.User;
import com.example.library.service.UserService;
import com.example.library.util.JwtUtil;
import com.example.library.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<String> login(@RequestBody User user) {
        logger.info("Login attempt for user: '{}'", user.getUsername());
        User dbUser = userService.findByUsername(user.getUsername());

        if (dbUser == null || !passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            logger.warn("Login failed for user '{}'. Bad credentials.", user.getUsername());
            return Result.error("用户名或密码错误");
        }
        
        // 登录成功, 获取用户角色
        List<String> roles = userService.findRolesByUsername(dbUser.getUsername());
        logger.info("Roles for user '{}': {}", dbUser.getUsername(), roles);
        
        // 生成包含角色信息的Token
        String token = jwtUtil.generateToken(dbUser.getUsername(), roles);
        return Result.success(token);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        boolean exists = userService.lambdaQuery().eq(User::getUsername, user.getUsername()).exists();
        if (exists) {
            return Result.error("用户名已存在");
        }
        if (userService.saveUser(user)) {
            return Result.success("注册成功");
        }
        return Result.error("注册失败，请联系管理员");
    }

    @GetMapping("/list")
    public Result<Page<User>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String username) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        queryWrapper.orderByDesc(User::getId);
        return Result.success(userService.page(page, queryWrapper));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody User user) {
        boolean exists = userService.lambdaQuery().eq(User::getUsername, user.getUsername()).exists();
        if (exists) {
            return Result.error("用户名 '" + user.getUsername() + "' 已存在");
        }
        if (userService.saveUser(user)) {
            return Result.success("新增成功");
        }
        return Result.error("新增失败");
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody User user) {
        // For security, password should not be updated directly here.
        // A separate method for password change is recommended.
        user.setPassword(null);
        return Result.success(userService.updateById(user));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(userService.deleteUser(id));
    }

    /**
     * 临时接口，用于为现有用户授予ADMIN角色。
     * 使用后建议删除或加以保护。
     * @param username 要授权的用户名
     * @return 操作结果
     */
    @PostMapping("/grant-admin/{username}")
    public Result<?> grantAdminRole(@PathVariable String username) {
        boolean result = userService.grantAdminRole(username);
        if (result) {
            return Result.success("成功为用户 '" + username + "' 授予ADMIN角色。");
        }
        return Result.error("操作失败，无法为用户 '" + username + "' 授予ADMIN角色。请检查后台日志。");
    }
} 