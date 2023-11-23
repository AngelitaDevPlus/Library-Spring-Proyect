package com.spring.library.service;

import com.spring.library.entity.Author;
import com.spring.library.entity.Book;
import com.spring.library.entity.Publisher;
import com.spring.library.exceptions.CostumizedException;
import com.spring.library.repository.AuthorRepository;
import com.spring.library.repository.BookRepository;
import com.spring.library.repository.PublisherRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    
    @Transactional
    public void createBook(Long isbn, String title, Integer copies, String idAuthor, String idPublisher) throws CostumizedException {
        
        validate(isbn, title, copies, idAuthor, idPublisher);
        
        Author author = authorRepository.findById(idAuthor).get();
        Publisher publisher = publisherRepository.findById(idPublisher).get();
        Book book = new Book();
        
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setCopies(copies);
        book.setActiveDate(new Date());
        
        book.setAuthor(author);
        book.setPublisher(publisher);
        
        bookRepository.save(book);
    }
    
    public List<Book> listBooks() {
        List<Book> books = new ArrayList();
        
        books = bookRepository.findAll();
        
        return books;
    }
    
    @Transactional
    public void modifyBook(Long isbn, String title, Integer copies, String idAuthor, String idPublisher) throws CostumizedException {
        
        validate(isbn, title, copies, idAuthor, idPublisher);
        
        Optional<Book> responseBook = bookRepository.findById(isbn);
        Optional<Author> responseAuthor = authorRepository.findById(idAuthor);
        Optional<Publisher> responsePublisher = publisherRepository.findById(idPublisher);
        
        Author author = new Author();
        Publisher publisher = new Publisher();
        
        if (responseAuthor.isPresent()) {
            
            author = responseAuthor.get();
        }
        
        if (responsePublisher.isPresent()) {
            
            publisher = responsePublisher.get();
        }
        
        if (responseBook.isPresent()) {
            
            Book book = responseBook.get();
            
            book.setTitle(title);
            book.setCopies(copies);
            book.setAuthor(author);
            book.setPublisher(publisher);
            
            bookRepository.save(book);
        }
    }
    
    private void validate(Long isbn, String title, Integer copies, String idAuthor, String idPublisher) throws CostumizedException {
        
        if (isbn == null) {
            throw new CostumizedException("The isbn can't be null.");
        }
        
        if (title.isEmpty() || title == null) {
            throw new CostumizedException("The title can't be null.");
        }
        
        if (copies == null) {
            throw new CostumizedException("The number of copies can't be null.");
        }
        
        if (idAuthor.isEmpty() || idAuthor == null) {
            throw new CostumizedException("The idAuthor can't be null.");
        }
        
        if (idPublisher.isEmpty() || idPublisher == null) {
            throw new CostumizedException("The idPublisher can't be null.");
        }
        
    }
}
