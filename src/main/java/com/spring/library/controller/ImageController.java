package com.spring.library.controller;

import com.spring.library.entity.AppUser;
import com.spring.library.service.AppUserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final AppUserService appUserService;

    @Autowired
    public ImageController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<byte[]> imageAppUser(@PathVariable("id") String idAppUser) {
        AppUser appUser = appUserService.getOneAppUser(idAppUser);
        byte[] image = appUser.getImage().getContent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
