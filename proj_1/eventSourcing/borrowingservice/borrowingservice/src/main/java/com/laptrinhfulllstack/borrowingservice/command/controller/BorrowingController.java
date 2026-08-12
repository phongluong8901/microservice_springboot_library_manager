package com.laptrinhfulllstack.borrowingservice.command.controller;

import java.util.Date;
import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhfulllstack.borrowingservice.command.command.CreateBorrowingCommand;
import com.laptrinhfulllstack.borrowingservice.command.model.BorrowingCreateModel;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowingController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String createBorrowing(@RequestBody BorrowingCreateModel model) {
        // Dùng new Date() để lấy đầy đủ cả ngày lẫn giờ, phút, giây hiện tại
        CreateBorrowingCommand command = new CreateBorrowingCommand(
                UUID.randomUUID().toString(),
                model.getBookId(),
                model.getEmployeeId(),
                new Date(),
                (Date) null);

        return commandGateway.sendAndWait(command);
    }
}