package com.ds.service;

import com.ds.domain.Book;
import com.ds.domain.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService{

    @Autowired
    private BookRepository bookRepository;

    /**
     * 全書籍一覧を取得
     * @return
     */
    public List<Book> findAll(){

        return bookRepository.findAll();

    }

    /**
     * 本を新規作成or更新
     * @param book
     * @return
     */
    public Book sava(Book book){
        return bookRepository.save(book);
    }

    /**
     * 本を１冊取得
     * @param id
     * @return
     */
    public Book getOne(long id){
        return bookRepository.getOne(id);
    }

    /**
     * 本を１冊削除
     * @param id
     */
    public void deleteOne(long id){
        bookRepository.deleteById(id);
    }

    /**
     *
     * @param author
     * @return
     */
    public List<Book> findByAuthor(String author){
       return bookRepository.findByAuthor(author);
    }


}
