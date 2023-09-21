package com.epam.rd.autotasks.springemployeecatalog.repositories.mappers;

import com.epam.rd.autotasks.springemployeecatalog.domain.Department;
import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import com.epam.rd.autotasks.springemployeecatalog.domain.FullName;
import com.epam.rd.autotasks.springemployeecatalog.domain.Position;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.sql.Types.NULL;

/**
 * @author Denys Parshutkin
 * @version 1.0.0
 */
@Component
public class EmployeeMapper {

    public Employee getEmployee(ResultSet resultSet, Employee manager) throws SQLException {
        Long id = resultSet.getLong("ID");
        String firstName = resultSet.getString("FIRSTNAME");
        String lastName = resultSet.getString("LASTNAME");
        String middleName = resultSet.getString("MIDDLENAME");
        Position position = Position.valueOf(resultSet.getString("POSITION").toUpperCase());
        LocalDate hired = resultSet.getDate("HIREDATE").toLocalDate();
        BigDecimal salary = resultSet.getBigDecimal("SALARY");
        FullName fullName = new FullName(firstName, lastName, middleName);
        Employee managerLocal = resultSet.getLong("MANAGER") == NULL ? null : manager;
        Department department = resultSet.getLong("DEPARTMENT") == NULL ? null : getDepartment(resultSet);
        return new Employee(id, fullName, position, hired, salary, managerLocal, department);
    }

    public Employee getManager(ResultSet resultSet) throws SQLException {
        int currentRow = resultSet.getRow();
        long managerID = resultSet.getLong("MANAGER");
        resultSet.beforeFirst();
        Employee manager = null;
        while (resultSet.next()) {
            if(resultSet.getLong("ID") == managerID){
                manager = getEmployee(resultSet, null);
                break;
            }
        }
        resultSet.absolute(currentRow);
        return manager;
    }

    public Employee getFullManager(ResultSet resultSet) throws SQLException {
        int currentRow = resultSet.getRow();
        long managerID = resultSet.getLong("MANAGER");
        resultSet.beforeFirst();
        Employee manager = null;
        while (resultSet.next()) {
            if(resultSet.getLong("ID") == managerID){
                manager = getEmployee(resultSet, getFullManager(resultSet));
                break;
            }
        }
        resultSet.absolute(currentRow);
        return manager;
    }


    public Department getDepartment(ResultSet resultSet) throws SQLException {
        Long depId = resultSet.getLong("DEPARTMENT");
        String name = resultSet.getString("NAME");
        String location = resultSet.getString("LOCATION");
        return new Department(depId, name, location);
    }

}
