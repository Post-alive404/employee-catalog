package com.epam.rd.autotasks.springemployeecatalog.repositories.extractors;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repositories.mappers.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */
@Component
public class ManagersResultSetExtractor implements ResultSetExtractor<List<Employee>> {
    private final EmployeeMapper employeeMapper;

    @Autowired
    public ManagersResultSetExtractor(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<Employee> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Employee> employees = new ArrayList<>();
        while (resultSet.next()) {
            employees.add(employeeMapper.getEmployee(resultSet, employeeMapper.getFullManager(resultSet)));
        }

        return employees;
    }
}
