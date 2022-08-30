package com.example.crudresttest.model.response;

import com.example.crudresttest.entity.Employee;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeesResponse {
    List<Employee> employees;
}
