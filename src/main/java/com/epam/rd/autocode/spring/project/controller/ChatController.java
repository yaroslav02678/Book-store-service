package com.epam.rd.autocode.spring.project.controller;


import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.impl.GeminiService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    private final GeminiService geminiService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    public ChatController(GeminiService geminiService, BookService bookService, ModelMapper modelMapper) {
        this.geminiService = geminiService;
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/ask")
    public String askGemini(@RequestParam("message") String message,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "6") int size,
                            @RequestParam(defaultValue = "name") String sortField,
                            @RequestParam(defaultValue = "asc") String sortDir,
                            Model model) {
        String answer = geminiService.askAssistant(message);

        model.addAttribute("userMessage", message);
        model.addAttribute("assistantAnswer", answer);

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
}
