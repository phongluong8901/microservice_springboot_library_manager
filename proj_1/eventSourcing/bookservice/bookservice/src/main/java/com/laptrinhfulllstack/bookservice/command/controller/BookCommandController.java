package com.laptrinhfulllstack.bookservice.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhfulllstack.bookservice.command.command.CreateBookCommand;
import com.laptrinhfulllstack.bookservice.command.command.DeleteBookCommand;
import com.laptrinhfulllstack.bookservice.command.command.UpdateBookCommand;
import com.laptrinhfulllstack.bookservice.command.model.BookRequestModel;

@RestController
@RequestMapping("/api/v1/books")
public class BookCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String addBook(@RequestBody BookRequestModel model) {
        try {
            System.out.println("====== DA VAO CONTROLLER: " + model.getName() + " ======");
            CreateBookCommand command = new CreateBookCommand(
                    UUID.randomUUID().toString(),
                    model.getName(),
                    model.getAuthor(),
                    true);
            return commandGateway.sendAndWait(command);
        } catch (Exception e) {
            // IN TOÀN BỘ LỖI RA TERMINAL ĐỂ XEM
            System.err.println("====== LOI XAY RA TRONG COMMAND GATEWAY ======");
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{bookId}")
    public String updateBook(@RequestBody BookRequestModel model, @PathVariable String bookId) {
        UpdateBookCommand command = new UpdateBookCommand(bookId, model.getName(), model.getAuthor(),
                model.getIsReady());
        return commandGateway.sendAndWait(command);
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable String bookId) {
        DeleteBookCommand command = new DeleteBookCommand(bookId);
        return commandGateway.sendAndWait(command);
    }
}