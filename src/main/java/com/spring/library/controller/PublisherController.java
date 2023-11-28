package com.spring.library.controller;

import com.spring.library.exceptions.CustomizedException;
import com.spring.library.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/publisher") //localhost:8080/publisher
public class PublisherController {
    
    @Autowired
    private PublisherService publisherService;
    
    @GetMapping("/register") //localhost:8080/publisher/register
    public String register() {
        return "publisher_form";
    }
    
    @PostMapping("/registered")
    public String registered(@RequestParam(required = false) String name, ModelMap model) {
        try {
            publisherService.createPublisher(name);
            model.put("success", "Publisher succesfuly uploaded! YEY!!!");
        } catch (CustomizedException ex) {
            model.put("error", "Publisher not uploaded! " + ex.getMessage());
            return "publisher_form";
        }
        return "publisher_form";
    }
  
    @GetMapping("/toList")
    public String publisherToList() {
        return "publisher_short_form"; //CREAR
    }
    
    @PostMapping("/oneListed")
    public String listOnePublisher(@RequestParam String name, ModelMap model) {
        try {
            model.addAttribute("publisher", publisherService.getOnePublisherByName(name));
            return "publisher_one"; // CREAR
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "publisher_short_form";
        }
    }
    
    @GetMapping("/list")
    public String listPublisher(ModelMap model) {
        model.addAttribute("publishers", publisherService.listPublishers());
        return "publisher_list"; // REVISAR
    }
    
    @GetMapping("/modify/{id}") //MODIFICAR
    public String modify(@PathVariable String id, ModelMap model) {
        model.put("publisher", publisherService.getOnePublisherById(id));
        return "publisher_modify";
    }
    
    @PostMapping("/modify/{id}") //MODIFICAR
    public String modify(@PathVariable String id, String name, ModelMap model) {
        try {
            publisherService.modifyPublisher(id, name);
            return "redirect:../list";
        } catch (CustomizedException ex) {
            model.put("error", ex.getMessage());
            return "publisher_modify";
        }
    }
}


