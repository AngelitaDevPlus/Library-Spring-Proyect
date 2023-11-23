package com.spring.library.service;

import com.spring.library.entity.Publisher;
import com.spring.library.exceptions.CostumizedException;
import com.spring.library.repository.PublisherRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherService {
    
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Transactional
    public void createPublisher(String name) throws CostumizedException {
        
        validate(name);
        
        Publisher publisher = new Publisher();
        
        publisher.setName(name);
        
        publisherRepository.save(publisher);
    }
    
    public List<Publisher> listPublishers() {
        
        List<Publisher> publishers = new ArrayList();
        
        publishers = publisherRepository.findAll();
                
        return publishers;
    }
    
    @Transactional
    public void modifyPublisher(String idPublisher, String name) throws CostumizedException {
        
        validate(name);
        
        Optional<Publisher> response = publisherRepository.findById(idPublisher);
        
        if (response.isPresent()) {
            
            Publisher publisher = response.get();
            
            publisher.setName(name);
            
            publisherRepository.save(publisher);
        }
    }
    private void validate(String name) throws CostumizedException {
        
        if (name.isEmpty() || name == null) {
            throw new CostumizedException("The Publisher's name can't be null.");
        }
        
    }
    
}
