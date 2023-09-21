package com.epam.rd.autotasks.springemployeecatalog.utility;

public class SQLqueries {
    public static final String GET_EMPLOYEE_BY_ID =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE id = %d" +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT * FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id";
    public static final String GET_ALL_EMPLOYEES_ORDER_BY = "SELECT E.ID, E.FIRSTNAME, E.LASTNAME, " +
            "E.MIDDLENAME, E.POSITION, E.HIREDATE AS hired, E.SALARY, E.MANAGER, E.DEPARTMENT, D.NAME, D.LOCATION FROM EMPLOYEE E " +
            "LEFT JOIN EMPLOYEE M ON E.MANAGER = M.ID LEFT JOIN DEPARTMENT D ON E.DEPARTMENT = D.ID ORDER BY %s %s";
    public static final String GET_EMPLOYEES_BY_MANAGER_ORDER_BY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE manager = %d " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";

    public static final String GET_EMPLOYEES_BY_DEP_ID_ORDER_BY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e WHERE department = %d " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";

    public static final String GET_EMPLOYEES_BY_DEP_NAME_ORDER_BY =
            "WITH RECURSIVE cte (id, firstName, lastName, middleName, position, manager, hireDate, salary, department, name, location) AS ( " +
                    "SELECT e.id, firstName, lastName, middleName, position, manager, hireDate, salary, department " +
                    "FROM employee e " +
                    "LEFT JOIN department d ON e.department = d.id " +
                    "WHERE d.name = '%s' " +
                    "UNION ALL " +
                    "SELECT m.id, m.firstName, m.lastName, m.middleName, m.position, m.manager, m.hireDate, m.salary, m.department " +
                    "FROM employee m " +
                    "INNER JOIN cte ON cte.manager = m.id " +
                    ") SELECT DISTINCT cte.id, firstName, lastName, middleName, position, manager, hireDate as hired, salary, department, name, location FROM cte " +
                    "LEFT JOIN department d ON cte.department = d.id " +
                    "ORDER BY %s %s";
}