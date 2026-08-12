package com.laptrinhfulllstack.bookservice.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.laptrinhfulllstack.bookservice.command.command.CreateBookCommand;
import com.laptrinhfulllstack.bookservice.command.command.DeleteBookCommand;
import com.laptrinhfulllstack.bookservice.command.command.UpdateBookCommand;
import com.laptrinhfulllstack.bookservice.command.event.BookCreatedEvent;
import com.laptrinhfulllstack.bookservice.command.event.BookDeletedEvent;
import com.laptrinhfulllstack.bookservice.command.event.BookUpdatedEvent;
import com.laptrinhfulllstack.commonservice.command.RollBackStatusBookCommand;
import com.laptrinhfulllstack.commonservice.command.UpdateStatusBookCommand;
import com.laptrinhfulllstack.commonservice.event.BookRollBackStatusEvent;
import com.laptrinhfulllstack.commonservice.event.BookUpdateStatusEvent;

import lombok.NoArgsConstructor;

@Aggregate
@NoArgsConstructor
public class BookAggregate {

    @AggregateIdentifier
    private String id;
    private String name;
    private String author;
    private Boolean isReady;

    // SỬ DỤNG CONSTRUCTOR DƯỚI ĐÂY ĐỂ AXON HIỂU ĐÂY LÀ LỆNH TẠO MỚI (CREATION
    // COMMAND HANDLER)
    @CommandHandler
    public BookAggregate(CreateBookCommand command) {
        BookCreatedEvent bookCreatedEvent = new BookCreatedEvent();
        BeanUtils.copyProperties(command, bookCreatedEvent);

        AggregateLifecycle.apply(bookCreatedEvent);
    }

    @CommandHandler
    public void handle(UpdateBookCommand command) {
        BookUpdatedEvent bookUpdatedEvent = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, bookUpdatedEvent);

        AggregateLifecycle.apply(bookUpdatedEvent);
    }

    @CommandHandler
    public void handle(DeleteBookCommand command) {
        BookDeletedEvent bookDeletedEvent = new BookDeletedEvent();
        BeanUtils.copyProperties(command, bookDeletedEvent);

        AggregateLifecycle.apply(bookDeletedEvent);
    }

    @CommandHandler
    public void handler(UpdateStatusBookCommand command) {
        BookUpdateStatusEvent event = new BookUpdateStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handler(RollBackStatusBookCommand command) {
        BookRollBackStatusEvent event = new BookRollBackStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookUpdatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(BookUpdateStatusEvent event) {
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookRollBackStatusEvent event) {
        this.id = event.getBookId();
        this.isReady = event.getIsReady();
    }
}