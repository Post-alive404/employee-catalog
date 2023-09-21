package com.epam.rd.autotasks.springemployeecatalog.controllers;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.services.EmployeeCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */
@RestController
@RequestMapping("employees")
public class EmployeeCatalogController {
    private final EmployeeCatalogService employeeCatalogService;

    @Autowired
    public EmployeeCatalogController(EmployeeCatalogService employeeCatalogService) {
        this.employeeCatalogService = employeeCatalogService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployees(Pageable pageable){
        return employeeCatalogService.getAllEmployees(pageable);
    }


    @GetMapping(value = "/{employee_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Employee> getEmployees(@PathVariable String employee_id,
                                           @RequestParam(required = false) boolean full_chain) {
        return employeeCatalogService.getEmployeeById(employee_id, full_chain);
    }

    @GetMapping(value = "/by_manager/{managerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployeesByManager(@PathVariable String managerId,
                                           Pageable pageable) {
        return employeeCatalogService.getEmployeesByManager(managerId, pageable);
    }

    @GetMapping(value = "by_department/{department}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getEmployeesByDepartment(@PathVariable String department,
                                                Pageable pageable) {
        return employeeCatalogService.getEmployeeByDepartment(department, pageable);
    }
}
