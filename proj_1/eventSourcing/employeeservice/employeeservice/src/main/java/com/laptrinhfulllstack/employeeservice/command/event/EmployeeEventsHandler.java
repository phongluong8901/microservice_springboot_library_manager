package com.laptrinhfulllstack.employeeservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhfulllstack.employeeservice.command.data.Employee;
import com.laptrinhfulllstack.employeeservice.command.data.EmployeeRepository;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmployeeEventsHandler {
    @Autowired
    private EmployeeRepository employeeRepository;

    @EventHandler
    public void on(EmployeeCreatedEvent event) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(event, employee);
        employeeRepository.save(employee);
    }

    @EventHandler
    public void on(EmployeeUpdateEvent event) {
        Optional<Employee> oldEmployee = employeeRepository.findById(event.getId());
        oldEmployee.ifPresent(employee -> {
            oldEmployee.orElseThrow(() -> new NotFoundException("Employee not found"));
            employee.setFirstName(event.getFirstName());
            employee.setKin(event.getKin());
            employee.setLastName(event.getLastName());
            employee.setIsDiscriplined(event.getIsDiscriplined());
            employeeRepository.save(employee);
        });
    }

    @EventHandler
    public void on(EmployeeDeletedEvent event) {
        try {
            employeeRepository.findById(event.getId())
                    .orElseThrow(() -> new NotFoundException("Employee is not found"));
            employeeRepository.deleteById(event.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
