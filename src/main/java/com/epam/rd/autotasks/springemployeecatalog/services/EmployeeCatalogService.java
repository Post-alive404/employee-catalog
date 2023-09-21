package com.epam.rd.autotasks.springemployeecatalog.services;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */
@Service
public class EmployeeCatalogService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeCatalogService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(Pageable pageable) {
        try {
            return employeeRepository.findAllEmployees(pageable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Employee> getEmployeeById(String id, boolean full_chain){
        if (full_chain){
            return employeeRepository.findEmployeeByIdWithManagers(Long.parseLong(id));
        } else {
            return employeeRepository.findEmployeeById(Long.parseLong(id));
        }
    }

    public List<Employee> getEmployeesByManager(String managerId, Pageable pageable){
        return employeeRepository.findEmployeesByManager(Long.parseLong(managerId), pageable);
    }

    public List<Employee> getEmployeeByDepartment(String department, Pageable pageable){
        if(department.matches("^\\d*$")){
            return employeeRepository.findEmployeesByDepartmentId(Long.parseLong(department), pageable);
        }else {
            return employeeRepository.findEmployeesByDepartmentName(department, pageable);
        }
    }

}
