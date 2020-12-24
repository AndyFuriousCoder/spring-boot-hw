package org.example.app.services;

import org.example.app.services.repository.ProjectRepository;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }

    public boolean saveBook(Book book) {
        if (
                isEmpty(book.getAuthor())
                && isEmpty(book.getTitle())
                && isNull(book.getSize())
        ) {
            return false;
        } else
            return bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeBooksByAuthor( String authorName) {
        return bookRepo.removeItemsByAuthor(authorName);
    }

    public boolean removeBooksByTitle( String title) {
        return bookRepo.removeItemsByTitle(title);
    }

    public boolean removeBooksBySize(Integer size) {
        return bookRepo.removeItemsBySize(size);
    }

    public List<Book> getFilteredBooks(String authorFilter, String titleFilter, Integer sizeFilter) {
        Stream<Book> books = getAllBooks().stream();

        if(!isEmpty(authorFilter)) {
            books = books.filter(book -> book.getAuthor().equals(authorFilter));
        }

        if(!isEmpty(titleFilter)) {
            books = books.filter(book -> book.getTitle().equals(titleFilter));
        }

        if(!isNull(sizeFilter)) {
            books = books.filter(book -> book.getSize().equals(sizeFilter));
        }

        return books.collect(Collectors.toList());
    }
}
