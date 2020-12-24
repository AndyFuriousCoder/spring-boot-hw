package org.example.app.services.repository;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public boolean store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        return repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeItemsByAuthor(String authorName) {
        retreiveAll().stream().filter(book -> book.getAuthor().equals(authorName)).forEach(book -> repo.remove(book));
        logger.info("Books with author name: " + authorName + " were removed");

        return retreiveAll().stream().anyMatch(book -> book.getAuthor().equals(authorName));
    }

    @Override
    public boolean removeItemsByTitle(String title) {
        retreiveAll().stream().filter(book -> book.getTitle().equals(title)).forEach(book -> repo.remove(book));
        logger.info("Books with title: " + title + " were removed");

        return retreiveAll().stream().anyMatch(book -> book.getTitle().equals(title));
    }

    @Override
    public boolean removeItemsBySize(Integer size) {
        retreiveAll().stream().filter(book -> book.getSize().equals(size)).forEach(book -> repo.remove(book));
        logger.info("Books with size: " + size + " were removed");

        return retreiveAll().stream().anyMatch(book -> book.getSize().equals(size));
    }
}
