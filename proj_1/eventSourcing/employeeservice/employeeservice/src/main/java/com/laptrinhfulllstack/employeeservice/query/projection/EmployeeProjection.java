package com.laptrinhfulllstack.employeeservice.query.projection;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.laptrinhfulllstack.commonservice.model.EmployeeResponseCommonModel;
import com.laptrinhfulllstack.employeeservice.command.data.Employee;
import com.laptrinhfulllstack.employeeservice.command.data.EmployeeRepository;
import com.laptrinhfulllstack.employeeservice.query.model.EmployeeResponseModel;
import com.laptrinhfulllstack.employeeservice.query.queries.GetAllEmployeeQuery;
import com.laptrinhfulllstack.commonservice.queries.GetDetailEmployeeQuery;

@Component
public class EmployeeProjection {
    @Autowired
    private EmployeeRepository employeeRepository;

    @QueryHandler
    public List<EmployeeResponseModel> handle(GetAllEmployeeQuery query) {
        List<Employee> listEmployees = employeeRepository.findAllByIsDiscriplined(query.getIsDiscriplined());
        return listEmployees.stream().map(employee -> {
            EmployeeResponseModel model = new EmployeeResponseModel();
            BeanUtils.copyProperties(employee, model);
            return model;
        }).toList();
    }

    @QueryHandler
    public EmployeeResponseCommonModel handle(GetDetailEmployeeQuery query) throws Exception {
        Employee employee = employeeRepository.findById(query.getId())
                .orElseThrow(() -> new Exception("Employee not found"));

        EmployeeResponseCommonModel model = new EmployeeResponseCommonModel();
        BeanUtils.copyProperties(employee, model);
        return model;
    }
}
