package com.spring.library.repository;

import com.spring.library.entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT l FROM Book l WHERE l.title = :title")
    public Book findByTitle(@Param("title") String title);
    
    @Query("SELECT a FROM Author a WHERE a.name =:name")
    public List<Book> findByAuthor(@Param("name") String name);
}
