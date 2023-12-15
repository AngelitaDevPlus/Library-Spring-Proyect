package com.spring.library.controller;

import com.spring.library.entity.AppUser;
import com.spring.library.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserService appUserService;

    @Autowired
    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/dashboard")
    public String adminPanel() {
        return "panel";
    }

    @GetMapping("/users")
    public String listUsers(ModelMap model) {
        List<AppUser> appUsers = appUserService.listUsers();
        model.addAttribute("appUsers", appUsers);
        return "user_list";
    }

    @GetMapping("/modifyRole/{id}")
    public String changeRole(@PathVariable String id){
        appUserService.changeRole(id);

        return "redirect:/admin/users";
    }
}
