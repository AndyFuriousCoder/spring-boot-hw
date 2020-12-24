package org.example.app.services.repository;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    boolean store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeItemsByAuthor(String authorName);

    boolean removeItemsByTitle(String title);

    boolean removeItemsBySize(Integer size);
}
