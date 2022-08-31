package com.example.crudresttest.service.impl;

import com.example.crudresttest.entity.Employee;
import com.example.crudresttest.exception.CustomException;
import com.example.crudresttest.model.response.GetEmployeesResponse;
import com.example.crudresttest.repository.EmployeeRepository;
import com.example.crudresttest.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new CustomException("Employee already exist with given email:" + employee.getEmail(), HttpStatus.BAD_REQUEST);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public GetEmployeesResponse getAllEmployees() {
        return GetEmployeesResponse.builder()
                .employees(employeeRepository.findAll())
                .build();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }
}
