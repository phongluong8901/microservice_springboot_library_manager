package com.laptrinhfulllstack.employeeservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhfulllstack.employeeservice.query.model.EmployeeResponseModel;
import com.laptrinhfulllstack.employeeservice.query.queries.GetAllEmployeeQuery;
import com.laptrinhfulllstack.employeeservice.query.queries.GetDetailEmployeeQuery;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    public List<EmployeeResponseModel> getAllEmployee(
            @RequestParam(required = false, defaultValue = "false") Boolean isDiscriplined) {
        return queryGateway.query(new GetAllEmployeeQuery(isDiscriplined),
                ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class)).join();
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponseModel getDetailemployee(@PathVariable String employeeId) {
        return queryGateway
                .query(new GetDetailEmployeeQuery(employeeId), ResponseTypes.instanceOf(EmployeeResponseModel.class))
                .join();
    }
}
