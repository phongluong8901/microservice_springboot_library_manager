package com.laptrinhfulllstack.bookservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhfulllstack.bookservice.command.data.Book;
import com.laptrinhfulllstack.bookservice.command.data.BookRepository;
import com.laptrinhfulllstack.commonservice.event.BookRollBackStatusEvent;
import com.laptrinhfulllstack.commonservice.event.BookUpdateStatusEvent;

@Component
public class BookEventsHandler {
    @Autowired
    private BookRepository bookRepository;

    @EventHandler
    public void on(BookCreatedEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event, book);

        bookRepository.save(book);
    }

    @EventHandler
    public void on(BookUpdatedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());

        if (oldBook.isPresent()) {
            Book book = oldBook.get();
            book.setAuthor(event.getAuthor());
            book.setName(event.getName());
            book.setIsReady(event.getIsReady());

            bookRepository.save(book);
        }
    }

    @EventHandler
    public void on(BookDeletedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getId());

        // if (oldBook.isPresent()) {
        // bookRepository.delete(oldBook.get());
        // }

        oldBook.ifPresent(bookRepository::delete);
    }

    @EventHandler
    public void on(BookUpdateStatusEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> {
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        });
    }

    @EventHandler
    public void on(BookRollBackStatusEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> {
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        });
    }

}
