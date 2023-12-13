package com.spring.library.controller;

import com.spring.library.entity.AppUser;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String index() { return "index"; }

    @GetMapping("/register")
    public String register() { return "register_form"; }

    @PostMapping("/registered")
    public String registered(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String password2,
                             ModelMap model) {
        try {
            appUserService.register(name, email, password, password2);
            model.put("success", "User successfully registered!!!");
        } catch (CustomizedException ex) {
            model.put("error", "User not registered. " + ex.getMessage());
            model.put("name", name);
            model.put("email", email);
            return "register_form";
        }
        return "register_form";
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap model) {
        if(error != null) {
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
}
