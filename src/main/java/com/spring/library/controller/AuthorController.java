package com.spring.library.controller;

import com.spring.library.exceptions.CustomizedException;
import com.spring.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/author") //localhost:8080/author
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;
    
    @GetMapping("/register") //localhost:8080/author/register
    public String register() {
        return "author_form";
    }
    
    @PostMapping("/registered")
    public String registered(@RequestParam(required = false) String name, ModelMap model) {
        try {
            authorService.createAuthor(name);
            model.put("success", "Author succesfuly uploaded! YEY!!!");
        } catch (CustomizedException ex) {
            model.put("error", "Author not uploaded! " + ex.getMessage()); 
            return "author_form";
        }
        return "author_form";
    }
    
    @GetMapping("/toList")
    public String authorToList() {
        return "author_short_form";
    }
    
    @PostMapping("/oneListed")
    public String listOneAuthor(@RequestParam String name, ModelMap model) {
        try {
            model.addAttribute("author", authorService.getOneAuthorByName(name));
            return "author_one";
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "author_short_form";
        }
    }
    
    @GetMapping("/list")
    public String listAuthor(ModelMap model) {
        model.addAttribute("authors", authorService.listAuthors());
        return "author_list";
    }
    
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable String id, ModelMap model) {
        model.put("author", authorService.getOneAuthorById(id));
        return "author_modify";
    }
    
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable String id, String name, ModelMap model) {
        try {
            authorService.modifyAuthor(id, name);
            return "redirect:../list";
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "author_modify";
        }
    }
}
