package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.aop.LoggableSecurityEvent;
import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @LoggableBusinessEvent("Перегляд списку книг")
    public String books(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "6") int size,
                        @RequestParam(defaultValue = "name") String sortField,
                        @RequestParam(defaultValue = "asc") String sortDir,
                        Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("books", bookService.getAllBooks(pageable));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);

        return "books/book-list";
    }

    @GetMapping("/search")
    @LoggableBusinessEvent("Пошук книг за ключовим словом")
    public String searchBooks(@RequestParam("q") String keyword,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "6") int size,
                              @RequestParam(defaultValue = "name") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDir,
                              Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("books", bookService.searchBooks(keyword, pageable));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);

        return "books/book-search";
    }

    @GetMapping("/{id}")
    @LoggableSecurityEvent(value = "Запит на редагування книги", level = Level.DEBUG)
    public String detail(@PathVariable long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "6") int size,
                         @RequestParam(defaultValue = "name") String sortField,
                         @RequestParam(defaultValue = "asc") String sortDir,
                         @RequestParam(name = "q", required = false) String keyword,
                         Model model) {

        BookDTO book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

        return "books/book-detail";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public String showEditForm(@PathVariable long id,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "6") int size,
                               @RequestParam(defaultValue = "name") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {

        BookDTO book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "books/book-edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    @LoggableSecurityEvent(value = "Запит на додавання нової книги", level = Level.DEBUG)
    public String updateBook(@PathVariable long id,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult bindingResult,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "6") int size,
                             @RequestParam(defaultValue = "name") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir) {

        if (bindingResult.hasErrors()) {
            return "books/book-edit";
        }

        bookService.updateBookById(id, bookDTO);

        return String.format("redirect:/books?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("book", new BookDTO());
        return "books/book-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String addBook(@Valid @ModelAttribute("book") BookDTO bookDTO,
                          BindingResult bindingResult,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "6") int size,
                          @RequestParam(defaultValue = "name") String sortField,
                          @RequestParam(defaultValue = "asc") String sortDir) {

        if (bindingResult.hasErrors()) {
            return "books/book-add";
        }

        bookService.addBook(bookDTO);

        return String.format("redirect:/books?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "6") int size,
                             @RequestParam(defaultValue = "name") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDir) {

        bookService.deleteBookById(id);

        return String.format("redirect:/books?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }
}
