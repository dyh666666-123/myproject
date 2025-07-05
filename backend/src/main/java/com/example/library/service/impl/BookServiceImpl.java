package com.example.library.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import com.example.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    
    // IService<Book> 已经提供了 saveBatch 的默认实现，
    // 我们可以在这里覆盖它以添加额外的逻辑，
    // 但如果只是简单调用父类方法，则此处的覆盖不是必须的。
    // 为了明确性，我们在此重写。
    @Override
    public boolean saveBatch(Collection<Book> entityList) {
        return super.saveBatch(entityList);
    }
} 