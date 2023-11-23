package com.spring.library.controller;

import com.spring.library.exceptions.CostumizedException;
import com.spring.library.service.AuthorService;
import com.spring.library.service.BookService;
import com.spring.library.service.PublisherService;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String register() {
        return "book_form.html";
    }
    
    @PostMapping("/registered")
    public String registered
            (@RequestParam(name = "isbn", required = false) Long isbn, 
            @RequestParam(name = "title", required = false) String title, 
            @RequestParam(name = "copies", required = false) Integer copies, 
            @RequestParam(name = "idAuthor", required = false) String idAuthor, 
            @RequestParam(name = "idPublisher", required = false) String idPublisher, ModelMap model) {
        
        try {
            bookService.createBook(isbn, title, copies, idAuthor, idPublisher);
            
            model.put("success", "Book succesfuly uploaded! YEY!!!");
            
            System.out.println(isbn + ", " 
                            + title + ", " 
                            + copies + ", " 
                            + idAuthor + ", " 
                            + idPublisher + " OK");
        } catch (CostumizedException ex) {
            
            model.put("error", ex.getMessage());
            //Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Null OK!");
            return "book_form.html";
        }
        
        System.out.println("Index OK");
        return "index.html";
    }
}
