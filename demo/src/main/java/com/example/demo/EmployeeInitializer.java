package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EmployeeInitializer {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @PostConstruct
    public void initializeEmployees() {
        Employee employee1 = new Employee();
        employee1.setFirstName("uday");
        employee1.setLastName("varma");
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("uday");
        employee2.setLastName("v");
        employeeRepository.save(employee2);
    }
}

