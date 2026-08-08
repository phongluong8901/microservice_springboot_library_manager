package com.laptrinhfulllstack.employeeservice.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.laptrinhfulllstack.employeeservice.command.command.CreateEmployeeCommand;
import com.laptrinhfulllstack.employeeservice.command.event.EmployeeCreatedEvent;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Aggregate
public class EmployeeAggregate {
    @AggregateIdentifier

    private String id;
    private String firstName;
    private String lastName;
    private String kin;
    private Boolean isDiscriplined;

    @CommandHandler
    public EmployeeAggregate(CreateEmployeeCommand command) {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(EmployeeCreatedEvent event) {
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.kin = event.getKin();
        this.isDiscriplined = event.getIsDiscriplined();
    }
}
