package com.spring.library.service;

import com.spring.library.entity.Author;
import com.spring.library.entity.Book;
import com.spring.library.entity.Publisher;
import com.spring.library.exceptions.CustomizedException;
import com.spring.library.repository.AuthorRepository;
import com.spring.library.repository.BookRepository;
import com.spring.library.repository.PublisherRepository;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
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
    public void createBook(Long isbn, String title, Integer copies, String idAuthor, String idPublisher) throws CustomizedException {
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
        return bookRepository.findAll();
    }

    @Transactional
    public void modifyBook(Long isbn, String title, Integer copies) throws CustomizedException {
        // IMPLEMENTAR QUE PASA SI SOLO QUIERO CAMBIAR UN VALOR
//        Optional<Author> responseAuthor = authorRepository.findById(idAuthor);
//        Author author = new Author();
//        if (responseAuthor.isPresent()) {
//            author = responseAuthor.get();
//        }
//
//        Optional<Publisher> responsePublisher = publisherRepository.findById(idPublisher);
//        Publisher publisher = new Publisher();
//        if (responsePublisher.isPresent()) {
//            publisher = responsePublisher.get();
//        }

        Optional<Book> responseBook = bookRepository.findById(isbn);
        if (responseBook.isPresent()) {
            Book book = responseBook.get();

            try {
                if (title != null) {
                    book.setTitle(title);
                }
                if (copies != null) {
                    book.setCopies(copies);
                }
//                if (publisher != null) {
//                    book.setPublisher(publisher);
//                }
//                if (author != null) {
//                    book.setAuthor(author);
//                }
                bookRepository.save(book);

            } catch (Exception e) {
                throw new CustomizedException("No modification made.");
            }
        }
    }

    public Book getOneBookById(Long isbn) {
        return bookRepository.findById(isbn).orElseThrow(
                () -> new NoSuchElementException("Book not found."));
    }

    public Book getOneBookByTitle(String title) throws CustomizedException {
        
        if (bookRepository.findByTitle(title) != null) {
            return bookRepository.findByTitle(title);
        } else {
            throw new CustomizedException("Book not found. Try again!");
        }
    }

    private void validate(Long isbn, String title, Integer copies, String idAuthor, String idPublisher) throws CustomizedException {
        if (isbn == null) {
            throw new CustomizedException("The isbn can't be null.");
        }
        if (title == null || title.isEmpty()) {
            throw new CustomizedException("The title can't be null.");
        }
        if (copies == null) {
            throw new CustomizedException("The number of copies can't be null.");
        }
        if (idAuthor == null || idAuthor.isEmpty()) {
            throw new CustomizedException("The idAuthor can't be null.");
        }
        if (idPublisher == null || idPublisher.isEmpty()) {
            throw new CustomizedException("The idPublisher can't be null.");
        }
    }
}
