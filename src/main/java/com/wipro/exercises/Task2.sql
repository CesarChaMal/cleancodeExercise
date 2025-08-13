/*
An organization maintains employment data in three tables: EMPLOYEE, COMPANY, and SALARY. Write a query to print the names of every company where the average salary is greater than or equal to 40000. Each distinct row of results in the output must contain the name of a company whose average employee salary is ≥ 40,000 in the COMPANY.NAME format

        Schema

        EMPLOYEE

        Name
        Type
        Description
        ID
        Integer
        An employee ID in the inclusive range [1, 1000]. This is the primary key.
        NAME
        String
        An employee name. This field contains between 1 and 100 characters (inclusive)

        Name
        Type
        Description
        ID
        Integer
        A company ID in the inclusive range [1, 1000]. This is the primary key.
        NAME
        String
        A company name. This field contains between 1 and 100 characters (inclusive).

        SALARY

        Name
        Type
        Description
        EMPLOYEE_ID
        Integer
        An employee ID in the inclusive range [1, 1000].
        COMPANY_ID
        Integer
        A company ID in the inclusive range [1, 1000].
        SALARY
        Integer
        The salary of the employee in the inclusive range [10000, 100000].

        Sample Data Tables

        EMPLOYEE

        ID	NAME
        1	Frances White
        2	Carolyn Bradley
        3	Annie Fernandez
        4	Ruth Hanson
        5	Paula Fuller
        6	Bonnie Johnston
        7	Ruth Gutierrez
        8	Ernest Thomas
        9	Joe Garza
        10	Anne Harris
        COMPANY

        ID	NAME
        1	PeopleSoft Inc
        2	Baker Hughes Incorporated
        3	MDU Resources Group Inc.
        4	DST Systems, Inc.
        5	Williams Companies Inc
        6	Fisher Scientific International Inc.
        7	Emcor Group Inc.
        8	Genuine Parts Company
        9	MPS Group Inc.
        10	Novellus Systems Inc
        SALARY

        EMPLOYEE_ID	COMPANY_ID	SALARY
        2	4	27779
        2	9	36330
        3	9	71466
        3	10	22804
        5	5	49892
        6	4	31493
        6	10	26888
        7	3	87118
        7	7	70767
        7	9	39929


        Sample Output

        MDU Resources Group Inc.
        Williams Companies Inc
        Emcor Group Inc.
        MPS Group Inc.

        Explanation

        The following companies have average employee salaries ≥ 40,000.

        MDU Resources Group Inc. has one employee (ID 7) whose SALARY is 87118. The company's average employee salary is 87118 ÷ 1 = 87118, and 87118 ≥ 40000.
        Williams Companies Inc has one employee (ID 5) whose SALARY is 49892. The company's average employee salary is 49892 ÷ 1 = 49892.
        Emcor Group Inc. has one employee (ID 7) whose SALARY is 70767. The company's average employee salary is 70767 ÷ 1 = 70767.
        MPS Group Inc. has three employees (IDs 2, 3, and 7) whose SALARY values are 36330, 71466, and 39929. The company's average employee salary (36330 + 71466 + 39929) ÷ 3 = 49241.67.

*/
        SELECT C.NAME
        FROM COMPANY C
        JOIN SALARY S ON C.ID = S.COMPANY_ID
        GROUP BY C.NAME
        HAVING AVG(S.SALARY) >= 40000;


