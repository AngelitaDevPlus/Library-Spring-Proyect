package com.spring.library.controller;

import com.spring.library.entity.Author;
import com.spring.library.entity.Publisher;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.service.AuthorService;
import com.spring.library.service.BookService;
import com.spring.library.service.PublisherService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private PublisherService publisherService;

    @GetMapping("/register") //localhost:8080/book/register
    public String register(ModelMap model) {
        List<Author> authors = authorService.listAuthors();
        List<Publisher> publishers = publisherService.listPublishers();
        
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        return "book_form.html";
    }

    @PostMapping("/registered")
    public String registered(@RequestParam(name = "isbn", required = false) Long isbn,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "copies", required = false) Integer copies,
            @RequestParam(name = "idAuthor", required = false) String idAuthor,
            @RequestParam(name = "idPublisher", required = false) String idPublisher, ModelMap model) {

        try {
            bookService.createBook(isbn, title, copies, idAuthor, idPublisher);
            model.put("success", "Book succesfuly uploaded! YEY!!!");
        } catch (CustomizedException ex) {
            List<Author> authors = authorService.listAuthors();
            List<Publisher> publishers = publisherService.listPublishers();

            model.addAttribute("authors", authors);
            model.addAttribute("publishers", publishers);

            model.put("error", "Book not uploaded! " + ex.getMessage());
            return "book_form";
        }
        return "book_form";
    }

    @GetMapping("/toList")
    public String bookToList() {
        return "book_short_form";
    }
    
    @PostMapping("/oneListed")
    public String listOneBook(@RequestParam String title, ModelMap model) {
        try {
            model.addAttribute("book", bookService.getOneBookByTitle(title));
            return "book_one";
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "book_short_form";
        }
    }

    @GetMapping("/list") //localhost:8080/book/register
    public String listBook(ModelMap model) {
        model.addAttribute("books", bookService.listBooks());
        return "book_list";
    }
    
    @GetMapping("/modify/{isbn}")
    public String modify(@PathVariable Long isbn, ModelMap model) {
        model.put("book", bookService.getOneBookById(isbn));
        return "book_modify";
    }
    
    @PostMapping("/modify/{isbn}")
    public String modify(@PathVariable Long isbn, String title, Integer copies, ModelMap model) {
        try {
            bookService.modifyBook(isbn, title, copies);
            return "redirect:../list";
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "book_modify";
        }
    }
    

}
