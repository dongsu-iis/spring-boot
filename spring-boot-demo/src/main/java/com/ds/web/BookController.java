package com.ds.web;

import com.ds.domain.Book;
import com.ds.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.jws.WebParam;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 一覧取得
     * @param model
     * @return
     */
    @GetMapping("/books")
    public String list(Model model) {

        List<Book> books = bookService.findAll();
        model.addAttribute("books",books);
        return "books";
    }

    /**
     * １件参照
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/books/{id}")
    public String detail(@PathVariable long id,
                         Model model) {

        Book book = bookService.findOneById(id);

        if (book == null) {
            book = new Book();
        }

        model.addAttribute("book", book);

        return "book";
    }

}
