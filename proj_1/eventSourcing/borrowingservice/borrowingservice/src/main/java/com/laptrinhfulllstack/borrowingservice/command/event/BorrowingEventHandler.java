package com.laptrinhfulllstack.borrowingservice.command.event;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhfulllstack.borrowingservice.command.data.Borrowing;
import com.laptrinhfulllstack.borrowingservice.command.data.BorrowingRepository;

@Component
public class BorrowingEventHandler {
    @Autowired
    private BorrowingRepository borrowingRepository;

    @EventHandler
    public void on(BorrowingCreatedEvent event) {
        Borrowing model = new Borrowing();
        model.setId(event.getId());
        model.setBorrowingDate(event.getBorrowingDate());
        model.setBookId(event.getBookId());
        model.setEmployeeId(event.getEmployeeId());
        borrowingRepository.save(model);
    }

    @EventHandler
    public void on(BorrowingDeletedEvent event) {
        Optional<Borrowing> oldEntry = borrowingRepository.findById(event.getId());
        oldEntry.ifPresent(borrowing -> borrowingRepository.delete(borrowing));
    }
}
