package com.example.crudresttest.service;

import com.example.crudresttest.entity.Employee;
import com.example.crudresttest.model.response.GetEmployeesResponse;

import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    GetEmployeesResponse getAllEmployees();

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee updatedEmployee);

    void deleteEmployee(long id);
}
