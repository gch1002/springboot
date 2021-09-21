package com.redis.controller;

import com.redis.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/book")
    public Book book() {
        Book book = new Book();
        book.setAuthor("路遥");
        book.setName("平凡的世界");
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("book", book);

        Book book1 = (Book) ops.get("book");
        return book1;
    }

    @GetMapping("/test")
    public String test() {
        ValueOperations<String, String> pos = stringRedisTemplate.opsForValue();
        pos.set("liubei", "刘备");

        String liubei = pos.get("liubei");
        return liubei;
    }
}