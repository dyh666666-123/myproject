package com.example.library.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.library.entity.Book;

import java.util.Collection;

public interface BookService extends IService<Book> {
    // MyBatis-Plus 默认提供了 saveBatch, 这里可以不写
    // 但为了清晰，可以保留
    boolean saveBatch(Collection<Book> entityList);
} 