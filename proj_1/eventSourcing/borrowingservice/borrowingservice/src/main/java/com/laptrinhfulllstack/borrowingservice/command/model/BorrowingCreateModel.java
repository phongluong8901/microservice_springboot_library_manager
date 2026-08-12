package com.laptrinhfulllstack.borrowingservice.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingCreateModel {

    private String bookId;
    private String employeeId;
}
