package com.spring.library.service;

import com.spring.library.entity.Publisher;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.repository.PublisherRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherService {
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Transactional
    public void createPublisher(String name) throws CustomizedException {
        validate(name);
        Publisher publisher = new Publisher();
        
        publisher.setName(name);
        publisherRepository.save(publisher);
    }
    
    public List<Publisher> listPublishers() {
        return publisherRepository.findAll();
    }
    
    @Transactional
    public void modifyPublisher(String idPublisher, String name) throws CustomizedException {
        validate(name);
        Optional<Publisher> response = publisherRepository.findById(idPublisher);
        
        if (response.isPresent()) {
            Publisher publisher = response.get();
            
            publisher.setName(name);
            publisherRepository.save(publisher);
        }
    }
    
    public Publisher getOnePublisherById(String idPublisher) {
        return publisherRepository.findById(idPublisher).orElseThrow(
                () -> new NoSuchElementException("Publisher not found."));
    }

    public Publisher getOnePublisherByName(String name) throws CustomizedException {
        validate(name);
        if (publisherRepository.findByName(name) != null) {
            return publisherRepository.findByName(name);
        } else {
            throw new CustomizedException("Publisher not found. Try again!");
        }
    }
    
    private void validate(String name) throws CustomizedException {
        if (name == null || name.isEmpty()) {
            throw new CustomizedException("The Publisher's name can't be null.");
        }
    }
    
}


