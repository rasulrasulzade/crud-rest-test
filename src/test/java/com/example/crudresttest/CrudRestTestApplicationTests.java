package com.example.crudresttest;

import com.example.crudresttest.entity.Employee;
import com.example.crudresttest.model.response.GetEmployeesResponse;
import com.example.crudresttest.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, args = "--spring.profiles.active=test")
@AutoConfigureMockMvc
class CrudRestTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setU() {
        employeeRepository.deleteAll();
    }

    @DisplayName("Create new employee")
    @Test
    public void testCreateEmployeeReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Pete")
                .lastName("Michell")
                .email("p.michell@gmail.com")
                .build();

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    @DisplayName("Get All employees")
    @Test
    public void testGetEmployees() throws Exception {
        // given - precondition or setup
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(Employee.builder().firstName("Chucky").lastName("Avtovagzal").email("cucky@gmail.com").build());
        employeeList.add(Employee.builder().firstName("Malish").lastName("Masazir").email("malish@gmail.com").build());
        employeeList.add(Employee.builder().firstName("Murquz").lastName("Masazir").email("murquz@gmail.com").build());

        employeeRepository.saveAll(employeeList);

        // when -  action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));

        // then - verify the output
        resultActions.andDo(print()).andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        GetEmployeesResponse response = objectMapper.readValue(contentAsString, GetEmployeesResponse.class);
        List<Employee> list = response.getEmployees();

        if (list.size() != employeeList.size()) fail();
        for (int i = 0; i < list.size(); i++) {
            Employee actual = list.get(i);
            Employee expected = employeeList.get(i);
            if (!actual.getFirstName().equals(expected.getFirstName())
                    || !actual.getLastName().equals(expected.getLastName())
                    || !actual.getEmail().equals(expected.getEmail()))
                fail();
        }

    }

    @DisplayName("GET employee by id - positive scenario")
    @Test
    public void getEmployeeByIdWithValidEmployeeId() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Pete")
                .lastName("Michell")
                .email("p.michell@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("GET employee by id - negative scenario")
    @Test
    public void getEmployeeByIdWithInvalidEmployeeId() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Update employee - positive scenario")
    @Test
    public void updateEmployeePositive() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Jordan")
                .lastName("Michell")
                .email("j.michell@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Nick")
                .lastName("Williams")
                .email("n.williams@gmail.com")
                .build();

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("Update employee - negative scenario")
    @Test
    public void updateEmployeeNegative() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Nick")
                .lastName("Williams")
                .email("n.williams@gmail.com")
                .build();

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete employee")
    @Test
    public void deleteEmployee() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Jordan")
                .lastName("Michell")
                .email("j.michell@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);


        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
