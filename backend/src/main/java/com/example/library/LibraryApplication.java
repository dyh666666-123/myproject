package com.example.library;

import com.example.library.entity.Role;
import com.example.library.service.RoleService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.library.mapper")
public class LibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    /**
     * 应用启动时，自动检查并初始化核心角色数据
     * @param roleService 角色服务
     * @return CommandLineRunner
     */
    @Bean
    public CommandLineRunner initRoles(RoleService roleService) {
        return args -> {
            List<String> requiredRoles = Arrays.asList("ADMIN", "USER");
            for (String roleNameStr : requiredRoles) {
                if (!roleService.lambdaQuery().eq(Role::getRoleName, roleNameStr).exists()) {
                    Role role = new Role();
                    role.setRoleName(roleNameStr);
                    roleService.save(role);
                    System.out.println(">>> Role '" + roleNameStr + "' initialized.");
                }
            }
        };
    }
} 