package com.tistory.whitepaek.demospringsecurityform.common;

import com.tistory.whitepaek.demospringsecurityform.account.Account;
import com.tistory.whitepaek.demospringsecurityform.account.AccountService;
import com.tistory.whitepaek.demospringsecurityform.book.Book;
import com.tistory.whitepaek.demospringsecurityform.book.BookRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataGenerator implements ApplicationRunner {

    private final AccountService accountService;
    private final BookRepository bookRepository;

    public DefaultDataGenerator(AccountService accountService, BookRepository bookRepository) {
        this.accountService = accountService;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO SEUNGJOO PAEK - IntelliJ IDEA 프로젝트에 활용하기
        // TODO WHITEPAEK - IntelliJ IDEA

        Account seungjoo_paek = createUser("SEUNGJOO PAEK");
        Account whitepaek = createUser("WHITEPAEK");

        createBook("IntelliJ IDEA 프로젝트에 활용하기", seungjoo_paek);
        createBook("IntelliJ IDEA", whitepaek);
    }

    private Account createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("1234");
        account.setRole("USER");
        return accountService.createNew(account);
    }

    private void createBook(String title, Account author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        bookRepository.save(book);
    }

}
