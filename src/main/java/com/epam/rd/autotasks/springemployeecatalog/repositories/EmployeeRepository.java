package com.epam.rd.autotasks.springemployeecatalog.repositories;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */
public interface EmployeeRepository {

    List<Employee> findAllEmployees(Pageable pageable) throws SQLException;
    Optional<Employee> findEmployeeById(Long id);
    Optional<Employee> findEmployeeByIdWithManagers(Long id);
    List<Employee> findEmployeesByManager(Long managerId, Pageable pageable);
    List<Employee> findEmployeesByDepartmentId(Long departmentId, Pageable pageable);
    List<Employee> findEmployeesByDepartmentName(String departmentName, Pageable pageable);


}
