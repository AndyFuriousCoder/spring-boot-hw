package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book, Model model) {
        if (bookService.saveBook(book)) {
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        } else {
            return books(model);
        }
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove, Model model) {
        if (bookService.removeBookById(bookIdToRemove)) {
            return "redirect:/books/shelf";
        } else {
            return books(model);
        }
    }

    @PostMapping("/remove-by-author")
    public String removeBooksByAuthor(@RequestParam(value = "author") String authorName, Model model) {
        if (bookService.removeBooksByAuthor(authorName)) {
            return "redirect:/books/shelf";
        } else {
            return books(model);
        }
    }

    @PostMapping("/remove-by-title")
    public String removeBooksByTitle(@RequestParam(value = "title") String title, Model model) {
        if (bookService.removeBooksByTitle(title)) {
            return "redirect:/books/shelf";
        } else {
            return books(model);
        }
    }

    @PostMapping("/remove-by-size")
    public String removeBooksBySize(@RequestParam(value = "size") Integer size, Model model) {
        if (bookService.removeBooksBySize(size)) {
            return "redirect:/books/shelf";
        } else {
            return books(model);
        }
    }

    @PostMapping("/filter")
    public String filterBooks(
            @RequestParam(value = "author") String author,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "size") Integer size,
            Model model
    ) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getFilteredBooks(author, title, size));
        logger.info("filter books");
        return "book_shelf";
    }
}
