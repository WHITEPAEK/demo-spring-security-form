package com.tistory.whitepaek.demospringsecurityform.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b from Book b where b.author.id = ?#{principal.account.id}")
    List<Book> findCurrentUserBooks();

}