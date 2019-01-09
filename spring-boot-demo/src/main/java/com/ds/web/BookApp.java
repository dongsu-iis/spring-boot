package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookApp {

    @Autowired
    private BookService bookService;

    /**
     * 書籍一覧を取得
     * @return
     */
    @GetMapping("/books")
    public List<Book> getAll(){

        return bookService.findAll();
    }

    /**
     * 新規作成
     * @return
     */
    @PostMapping("/books")
    public Book post(Book book){

//        Book book = new Book();
//        book.setName(name);
//        book.setAuthor(author);
//        book.setDesctiption(description);
//        book.setStatus(status);

        return bookService.sava(book);
    }


    /**
     * 1件の本を取得
     * @param id
     * @return
     */
    @GetMapping("/books/{id}")
    public Book getOne(@PathVariable long id){

        return bookService.getOne(id);

    }

    /**
     * 更新処理
     * @param id
     * @param name
     * @param author
     * @param description
     * @param status
     * @return
     */
    @PutMapping("/books")
    public Book update(@RequestParam long id,
                       @RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String description,
                       @RequestParam int status){

        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setDesctiption(description);
        book.setStatus(status);

        return bookService.sava(book);

    }

    /**
     * 本を１冊削除
     * @param id
     */
    @DeleteMapping("/books/{id}")
    public void deleteOne(@PathVariable long id){
        bookService.deleteOne(id);
    }

    @PostMapping("/books/by")
    public List<Book> findBy(@RequestParam String author){

        return bookService.findByAuthor(author);

    }
}
