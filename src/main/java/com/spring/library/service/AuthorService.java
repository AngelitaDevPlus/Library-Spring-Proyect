package com.spring.library.service;

import com.spring.library.entity.Author;
import com.spring.library.exceptions.CostumizedException;
import com.spring.library.repository.AuthorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Transactional
    public void createAuthor(String name) throws CostumizedException {
        
        validate(name);
        
        Author author = new Author();
        
        author.setName(name);
        
        authorRepository.save(author);
    }
    
    public List<Author> listAuthors(){
        
        List<Author> authors = new ArrayList();
        
        authors = authorRepository.findAll();
        
        return authors;
    }
    
    @Transactional
    public void modifyAuthor(String idAuthor, String name) throws CostumizedException {
        
        validate(name);
        
        Optional<Author> response = authorRepository.findById(idAuthor);
        
        if (response.isPresent()) {
            
            Author author = response.get();
            
            author.setName(name);
            
            authorRepository.save(author);
        }
    }
    
    private void validate(String name) throws CostumizedException {
        
        if (name.isEmpty() || name == null) {
            throw new CostumizedException("The Author's name can't be null.");
        }
        
    }
    
}
