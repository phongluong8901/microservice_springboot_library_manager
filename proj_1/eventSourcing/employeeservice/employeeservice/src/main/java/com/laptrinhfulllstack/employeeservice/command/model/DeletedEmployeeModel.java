package com.laptrinhfulllstack.employeeservice.command.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedEmployeeModel {
    @NotBlank(message = "ID is mandatory")
    private String id;
}
