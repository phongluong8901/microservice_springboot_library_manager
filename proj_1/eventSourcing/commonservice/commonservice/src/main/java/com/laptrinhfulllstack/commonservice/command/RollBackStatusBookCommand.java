package com.laptrinhfulllstack.commonservice.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RollBackStatusBookCommand {
    @TargetAggregateIdentifier
    private String bookId;
    private Boolean isReady;
    private String employeeId;
    private String borrowingId;
}
