package com.epam.rd.autotasks.springemployeecatalog.repositories;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.repositories.extractors.EmployeeResultSetExtractor;
import com.epam.rd.autotasks.springemployeecatalog.repositories.extractors.ManagersResultSetExtractor;
import com.epam.rd.autotasks.springemployeecatalog.utility.SQLqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{
    private final JdbcTemplate jdbc;
    private final EmployeeResultSetExtractor employeeResultSetExtractor;
    private final ManagersResultSetExtractor managersResultSetExtractor;

    @Autowired
    public EmployeeRepositoryImpl(JdbcTemplate jdbc, EmployeeResultSetExtractor employeeResultSetExtractor, ManagersResultSetExtractor managersResultSetExtractor) {
        this.jdbc = jdbc;
        this.employeeResultSetExtractor = employeeResultSetExtractor;
        this.managersResultSetExtractor = managersResultSetExtractor;
    }


    @Override
    public List<Employee> findAllEmployees(Pageable pageable) {
        String sql = String.format(SQLqueries.GET_ALL_EMPLOYEES_ORDER_BY, this.getSort(pageable), this.getDirection(pageable));
        return this.getSublist(jdbc.query(getPreparedStatementCreator(sql) ,employeeResultSetExtractor), pageable);
    }

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        String sql = String.format(SQLqueries.GET_EMPLOYEE_BY_ID, id);
        return Optional.of(Objects.requireNonNull(jdbc.query(getPreparedStatementCreator(sql), employeeResultSetExtractor)).get(0));
    }

    @Override
    public Optional<Employee> findEmployeeByIdWithManagers(Long id) {
        String sql = String.format(SQLqueries.GET_EMPLOYEE_BY_ID, id);
        return Optional.of(Objects.requireNonNull(jdbc.query(getPreparedStatementCreator(sql), managersResultSetExtractor)).get(0));
    }

    @Override
    public List<Employee> findEmployeesByManager(Long managerId, Pageable pageable) {
        String sql = String.format(SQLqueries.GET_EMPLOYEES_BY_MANAGER_ORDER_BY, managerId, this.getSort(pageable), this.getDirection(pageable));
        return this.getSublist(Objects.requireNonNull(jdbc.query(getPreparedStatementCreator(sql), employeeResultSetExtractor)).
                stream().filter(employee -> employee.getManager() != null && employee.getManager().getId().equals(managerId)).
                collect(Collectors.toList()), pageable);
    }

    @Override
    public List<Employee> findEmployeesByDepartmentId(Long departmentId, Pageable pageable) {
        String sql = String.format(SQLqueries.GET_EMPLOYEES_BY_DEP_ID_ORDER_BY, departmentId, this.getSort(pageable), this.getDirection(pageable));
        return this.getSublist(Objects.requireNonNull(jdbc.query(getPreparedStatementCreator(sql), employeeResultSetExtractor)).stream()
                .filter(employee -> employee.getManager() != null && employee.getDepartment().getId().equals(departmentId))
                .collect(Collectors.toList()), pageable);
    }

    @Override
    public List<Employee> findEmployeesByDepartmentName(String departmentName, Pageable pageable) {
        String sql = String.format(SQLqueries.GET_EMPLOYEES_BY_DEP_NAME_ORDER_BY, departmentName, this.getSort(pageable), this.getDirection(pageable));
        return this.getSublist(Objects.requireNonNull(jdbc.query(getPreparedStatementCreator(sql), employeeResultSetExtractor)).stream()
                .filter(employee -> employee.getDepartment() != null && employee.getDepartment().getName().equals(departmentName))
                .collect(Collectors.toList()), pageable);
    }

    private PreparedStatementCreator getPreparedStatementCreator(String pageableQuery) {
        return connection -> connection.prepareStatement(pageableQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    private String getSort(Pageable pageable){
        return pageable.getSort().stream().findFirst().isPresent() ? pageable.getSort().stream().findFirst().get().getProperty() : "ID";
    }
    private String getDirection(Pageable pageable){
        return pageable.getSort().stream().findFirst().isPresent() ? pageable.getSort().stream().findFirst().get().getDirection().name() : "ASC";
    }
    private List<Employee> getSublist(List<Employee> employees, Pageable pageable) {
        int fromIndex = getFromIndex(pageable);
        int toIndex = getToIndex(pageable, employees, fromIndex);
        if (fromIndex > toIndex) {
            return Collections.emptyList();
        } else {
            return Objects.requireNonNull(employees).subList(fromIndex, toIndex);
        }
    }

    private int getToIndex(Pageable pageable, List<Employee> employees, int fromIndex) {
        return Math.min(fromIndex + pageable.getPageSize(), Objects.requireNonNull(employees).size());
    }

    private int getFromIndex(Pageable pageable) {
        return pageable.getPageNumber() * pageable.getPageSize();
    }
}
