package com.example.library.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.entity.Book;
import com.example.library.entity.BookCategory;
import com.example.library.service.BookService;
import com.example.library.service.BookCategoryService;
import com.example.library.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookCategoryService bookCategoryService;

    @GetMapping("/list")
    public Result<Page<Book>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String title) {
        Page<Book> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like(Book::getTitle, title);
        }
        queryWrapper.orderByDesc(Book::getId);
        return Result.success(bookService.page(page, queryWrapper));
    }

    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody Book book) {
        return Result.success(bookService.save(book));
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Book book) {
        return Result.success(bookService.updateById(book));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(bookService.removeById(id));
    }

    @GetMapping("/category-tree")
    public Result<List<BookCategory>> categoryTree() {
        return Result.success(bookCategoryService.list());
    }

    @GetMapping("/validate-isbn")
    public Result<Boolean> validateIsbn(@RequestParam String isbn) {
        // 简单校验逻辑
        boolean valid = isbn != null && (isbn.length() == 10 || isbn.length() == 13);
        return Result.success(valid);
    }
} 