package com.example.library.controller;

import com.example.library.entity.BookCategory;
import com.example.library.service.BookCategoryService;
import com.example.library.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-category")
public class BookCategoryController {

    @Autowired
    private BookCategoryService bookCategoryService;

    @GetMapping("/list")
    public Result<List<BookCategory>> list() {
        return Result.success(bookCategoryService.list());
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody BookCategory category) {
        return Result.success(bookCategoryService.save(category));
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody BookCategory category) {
        return Result.success(bookCategoryService.updateById(category));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        // TODO: Check if category is in use before deleting
        return Result.success(bookCategoryService.removeById(id));
    }
} 