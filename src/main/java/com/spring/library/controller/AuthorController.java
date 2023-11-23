package com.spring.library.controller;

import com.spring.library.exceptions.CostumizedException;
import com.spring.library.service.AuthorService;
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
@RequestMapping("/author") //localhost:8080/author
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;
    
    @GetMapping("/register") //localhost:8080/author/register
    public String register() {
        return "author_form.html";
    }
    
    @PostMapping("/registered")
    public String registered(@RequestParam(required = false) String name, ModelMap model) {
        
        try {
            authorService.createAuthor(name);
            
            model.put("success", "Author succesfuly uploaded! YEY!!!");
            
            System.out.println(name + " OK");
        } catch (CostumizedException ex) {
            
            model.put("error", ex.getMessage()); 
            //Logger.getLogger(AuthorController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Null OK!");
            return "author_form.html";
        }
        
        System.out.println("Index OK");
        return "index.html";
    }
}
