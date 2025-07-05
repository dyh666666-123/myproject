package com.example.library.controller;

import com.example.library.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @GetMapping("/top10")
    public Result<List<Map<String, Object>>> top10() {
        // 示例数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "书" + i);
            map.put("value", 10 - i + 1);
            list.add(map);
        }
        return Result.success(list);
    }

    @GetMapping("/category-stats")
    public Result<List<Map<String, Object>>> categoryStats() {
        // 示例数据
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(Map.of("name", "A类", "value", 10));
        list.add(Map.of("name", "B类", "value", 8));
        return Result.success(list);
    }
} 