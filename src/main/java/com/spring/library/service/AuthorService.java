package com.spring.library.service;

import com.spring.library.entity.Author;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.repository.AuthorRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Transactional
    public void createAuthor(String name) throws CustomizedException {
        validate(name);
        Author author = new Author();
        
        author.setName(name);
        authorRepository.save(author);
    }
    
    public List<Author> listAuthors(){
        return authorRepository.findAll();
    }
    
    @Transactional
    public void modifyAuthor(String idAuthor, String name) throws CustomizedException {
        validate(name);
        Optional<Author> response = authorRepository.findById(idAuthor);
        
        if (response.isPresent()) {
            Author author = response.get();
            
            author.setName(name);
            authorRepository.save(author);
        }
    }
    
    public Author getOneAuthorById(String idAuthor) {
        return authorRepository.findById(idAuthor).orElseThrow( 
                () -> new NoSuchElementException("Author not found."));
    }
    
    public Author getOneAuthorByName(String name) throws CustomizedException {
        validate(name);
        if (authorRepository.findByName(name) != null) {
            return authorRepository.findByName(name);
        } else {
            throw new CustomizedException("Author not found. Try again!");
        }
    }
    
    private void validate(String name) throws CustomizedException {
        if (name == null || name.isEmpty()) {
            throw new CustomizedException("The Author's name can't be null.");
        }
    }
    
}
