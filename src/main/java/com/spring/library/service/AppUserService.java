package com.spring.library.service;

import com.spring.library.entity.AppUser;
import com.spring.library.entity.Image;
import com.spring.library.enums.Role;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ImageService imageService;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, ImageService imageService) {
        this.appUserRepository = appUserRepository;
        this.imageService = imageService;
    }

    @Transactional
    public void register(MultipartFile file, String name, String email, String password, String password2) throws CustomizedException {
        validate(name, email, password, password2);

        AppUser appUser = new AppUser();
        appUser.setName(name);
        appUser.setEmail(email);
        appUser.setPassword(new BCryptPasswordEncoder().encode(password));
        appUser.setRole(Role.USER); // Predetermine Role as User
        Image image = imageService.save(file);
        appUser.setImage(image);
        appUserRepository.save(appUser);
    }

    @Transactional
    public void update(MultipartFile file, String idAppUser, String name, String email, String password, String password2) throws CustomizedException {
        validate(name, email, password, password2);

        Optional<AppUser> response = appUserRepository.findById(idAppUser);
        if (response.isPresent()) {
            AppUser appUser = response.get();
            appUser.setName(name);
            appUser.setEmail(email);
            appUser.setPassword(new BCryptPasswordEncoder().encode(password));
            appUser.setRole(Role.USER); // Predetermine Role as User

            String idImage = null;
            if (appUser.getImage() != null) {
                idImage = appUser.getImage().getId();
            }
            Image image = imageService.update(file, idImage);
            appUser.setImage(image);
            appUserRepository.save(appUser);
        }
    }

    public AppUser getOneAppUser(String idAppUser) {
        return appUserRepository.getReferenceById(idAppUser);
    }
    private void validate(String name, String email, String password, String password2) throws CustomizedException {
        if (name == null || name.isEmpty()) {
            throw new CustomizedException("The name can't be null.");
        }
        if (email == null || email.isEmpty()) {
            throw new CustomizedException("The email can't be null.");
        }
        if ((password == null || password.isEmpty()) || password.length() <= 5) {
            throw new CustomizedException("The password must have more than 5 characters.");
        }
        if (!password.equals(password2)) {
            throw new CustomizedException("Password must be the same.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findUserByEmail(email);

        if (appUser != null) {
            List<GrantedAuthority> permissions = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + appUser.getRole().toString()); // ROLE_USER
            permissions.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("appUserSession", appUser);
            return new User(appUser.getEmail(), appUser.getPassword(), permissions);
        } else {
            return null;
        }
    }
}
