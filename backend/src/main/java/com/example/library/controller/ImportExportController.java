package com.example.library.controller;

import com.alibaba.excel.EasyExcel;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import com.example.library.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class ImportExportController {
    @Autowired
    private BookService bookService;

    @PostMapping("/import")
    public Result<String> importBooks(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }
        try {
            List<Book> books = EasyExcel.read(file.getInputStream()).head(Book.class).sheet().doReadSync();
            if (books.isEmpty()) {
                return Result.error("Excel文件内容为空");
            }
            bookService.saveBatch(books);
            return Result.success("成功导入 " + books.size() + " 条图书数据");
        } catch (IOException e) {
            return Result.error("文件读取失败: " + e.getMessage());
        } catch (Exception e) {
            return Result.error("数据导入失败，请检查文件格式或内容是否正确: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBooks() {
        // 1. 获取图书列表
        List<Book> books = bookService.list();

        // 2. 在内存中创建Excel文件
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        EasyExcel.write(out, Book.class).sheet("图书列表").doWrite(books);

        // 3. 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "books.xlsx");

        // 4. 将Excel文件的二进制数据作为响应体返回
        return new ResponseEntity<>(out.toByteArray(), headers, 200);
    }
} 