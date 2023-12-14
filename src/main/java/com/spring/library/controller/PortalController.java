package com.spring.library.controller;

import com.spring.library.entity.AppUser;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class PortalController {

    private final AppUserService appUserService;

    @Autowired
    public PortalController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String register() {
        return "register_form";
    }

    @PostMapping("/registered")
    public String registered(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            ModelMap model,
            MultipartFile file) {
        try {
            appUserService.register(file, name, email, password, password2);
            model.put("success", "User successfully registered!!!");
            return "index";
        } catch (CustomizedException ex) {
            model.put("error", "User not registered. " + ex.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "register_form";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if (error != null) {
            model.put("error", "User or Password invalid.");
        }
        return "login";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/welcomepage") // Inicio
    public String welcomePage(HttpSession session) {
        AppUser logedAppUser = (AppUser) session.getAttribute("appUserSession");
        if (logedAppUser.getRole().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "welcomepage";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/profile")
    public String profile(ModelMap model, HttpSession session) {
        AppUser appUser = (AppUser) session.getAttribute("appUserSession");
            model.put("appUser", appUser);
        return "user_modify";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/profile/{id}")
    public String update(@PathVariable("id") String idAppUser,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String password2,
                         ModelMap model,
                         MultipartFile file) {
        try {
            appUserService.update(file, idAppUser, name, email, password, password2);
            model.put("success", "User updated correctly!");
            return "welcomepage";
        } catch (CustomizedException ex) {
            model.put("error", "User not updated. " + ex.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "user_modify";
        }
    }
}
