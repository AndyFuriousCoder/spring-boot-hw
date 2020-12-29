package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.EmptyFileUploadException;
import org.example.app.exceptions.IncorrectFilterException;
import org.example.app.services.BookService;
import org.example.app.services.FileOperationsService;
import org.example.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private static final String ATTRIBUTE_BOOK = "book",
                                ATTRIBUTE_BOOK_ID_TO_REMOVE = "bookIdToRemove",
                                ATTRIBUTE_AUTHOR_NAME_TO_REMOVE = "authorNameToRemove",
                                ATTRIBUTE_TITLE_TO_REMOVE = "titleToRemove",
                                ATTRIBUTE_SIZE_TO_REMOVE = "sizeToRemove",
                                ATTRIBUTE_BOOK_LIST = "bookList";

    private final Logger logger = Logger.getLogger(BookShelfController.class);

    private final BookService bookService;
    private final FileOperationsService fileOperationsService;

    @Autowired
    public BookShelfController(BookService bookService, FileOperationsService fileOperationsService) {
        this.bookService = bookService;
        this.fileOperationsService = fileOperationsService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute(ATTRIBUTE_BOOK, new Book());
        model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
        model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
        model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
        model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
        model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        logger.info("Errors: " + bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_BOOK, book);
            model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
            model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
            model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
            model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
            model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_BOOK, new Book());
            model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
            model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
            model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
            model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove-by-author")
    public String removeBooksByAuthor(@Valid AuthorNameToRemove authorNameToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_BOOK, new Book());
            model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
            model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
            model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
            model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
            return "book_shelf";
        } else {
            if (bookService.removeBooksByAuthor(authorNameToRemove.getAuthor())) {
                return "redirect:/books/shelf";
            } else {
                return books(model);
            }
        }

    }

    @PostMapping("/remove-by-title")
    public String removeBooksByTitle(@Valid TitleToRemove titleToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_BOOK, new Book());
            model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
            model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
            model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
            model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
            return "book_shelf";
        } else {
            if (bookService.removeBooksByTitle(titleToRemove.getTitle())) {
                return "redirect:/books/shelf";
            } else {
                return books(model);
            }
        }
    }

    @PostMapping("/remove-by-size")
    public String removeBooksBySize(@Valid SizeToRemove sizeToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(ATTRIBUTE_BOOK, new Book());
            model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
            model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
            model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
            model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getAllBooks());
            return "book_shelf";
        } else {
            if (bookService.removeBooksBySize(sizeToRemove.getSize())) {
                return "redirect:/books/shelf";
            } else {
                return books(model);
            }
        }
    }

    @PostMapping("/filter")
    public String filterBooks(
            @RequestParam(value = "author") String author,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "size") Integer size,
            Model model
    ) throws IncorrectFilterException {
        if (isEmpty(author) && isEmpty(title) && isNull(size)) {
            throw new IncorrectFilterException();
        }
        model.addAttribute(ATTRIBUTE_BOOK, new Book());
        model.addAttribute(ATTRIBUTE_BOOK_ID_TO_REMOVE, new BookIdToRemove());
        model.addAttribute(ATTRIBUTE_AUTHOR_NAME_TO_REMOVE, new AuthorNameToRemove());
        model.addAttribute(ATTRIBUTE_TITLE_TO_REMOVE, new TitleToRemove());
        model.addAttribute(ATTRIBUTE_SIZE_TO_REMOVE, new SizeToRemove());
        model.addAttribute(ATTRIBUTE_BOOK_LIST, bookService.getFilteredBooks(author, title, size));
        logger.info("filter books");
        return "book_shelf";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            logger.info("File to upload is not selected");
            throw new EmptyFileUploadException();
        }
        String absolutePath = fileOperationsService.uploadFile(file);
        logger.info("new file saved at: " + absolutePath);
        return "redirect:/books/shelf";
    }

    // to test this controller need to upload some file first
    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("file") String file) throws Exception {
        if (!fileOperationsService.getAvailableFileNames().contains(file)) {
            logger.info("Requested file with name '" + file + "' does not exists");
            throw new FileNotFoundException("File with name '" + file + "' does not exists");
        }

        File fileToDownload = fileOperationsService.getFileToDownload(file);
        logger.info("file downloaded form: " + fileToDownload.getAbsolutePath());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileToDownload.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(fileToDownload));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @ExceptionHandler(EmptyFileUploadException.class)
    public String handleUploadFileError(Model model, EmptyFileUploadException exception) {
        model.addAttribute("uploadErrorMessage", exception.getMessage());
        return books(model);
    }

    @ExceptionHandler(IncorrectFilterException.class)
    public String handleIncorrectFilterError(Model model, IncorrectFilterException exception) {
        model.addAttribute("filterErrorMessage", exception.getMessage());
        return books(model);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public String handleFileNotFoundError(Model model, FileNotFoundException exception) {
        model.addAttribute("fileNotFoundErrorMessage", exception.getMessage());
        return books(model);
    }
}
