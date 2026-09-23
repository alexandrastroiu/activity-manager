# Activity Manager

**Activity Manager** is a web platform designed to help teachers manage courses, record attendance, organize teaching activities, and monitor progress on tasks and subtasks through a dashboard.

## Technologies

- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA (Hibernate)
- **Frontend:** Thymeleaf, CSS, JavaScript
- **Database:** MySQL

## Demo

**Activity Manager Dashboard**:

![Activity Manager dashboard](/images/dashboard.png)

**Video Demo**:

[Activity Manager Demo](https://youtu.be/HmF-j-mYedU)

## Key Features

* Login using existing teacher accounts stored in the database and logout ***(Session-based authentication)***
* Dashboard with courses, course sessions, attendance, activities and statistics
* Input validation
* View all courses for the logged-in teacher
* View all sessions for the selected course
* Create/Edit sessions for the selected course
* View all students enrolled in the selected course and mark attendance for the selected session
* Create/Edit/Delete activities and subtasks
* Filter activities by criteria: status, priority, difficulty, deadline
* View a chart showing activity progress
* View statistics: total activities, completed activities, average progress

## Architecture

Activity Manager follows the **Model–View–Controller (MVC)** architecture:

- **Model:** Represents application data and its relationships, stored in the MySQL database.
- **View:** Thymeleaf templates render the pages, with CSS for styling and JavaScript for the progress chart.
- **Controller:** Spring Boot controllers handle requests and connect the views to the application logic.

The application also has a service layer for business logic and a repository layer for database access.
Controllers use  Data Transfer Objects (DTOs) to pass data to and from the service layer, keeping request and response data separate from JPA entities.
Teacher authentication and access rules are implemented with Spring Security.

## Database Design

Activity Manager uses **MySQL** to store user accounts, teacher information, student information, departments, student groups, courses, course sessions, course enrollments, attendance records, and teaching activities and subtasks.

**ER Diagram:**

![Activity Manager ER diagram](/database/ER_diagram.png)

## Run Locally

### Prerequisites

* Java 21
* MySQL Server 8.0
* MySQL Workbench or another database management tool

### Setup

1. Clone the repository

    ```
    git clone https://github.com/alexandrastroiu/activity-manager.git
    cd activity-manager/
    ```

2. Create and populate the MySQL database

    Set up a new MySQL connection using MySQL Workbench, configure the database credentials and connect to MySQL Server.
    Execute the DDL script first `database/schema.sql` to create the database and tables, then execute the SQL script `database/data.sql` to insert the sample data.

3. Configure environment variables

    Copy the example environment file and enter your MySQL database credentials:


     ```bash
    cp .env.example .env
    ```
    

4. Load the environment variables
   Run in the terminal:

    ```bash
    set -a
    source .env
    set +a
    ```

5. Start the application
   From the repository root, run:

    ```bash
    ./mvnw spring-boot:run
    ```

6. Open the application and sign in

    Visit [http://localhost:8080](http://localhost:8080) (or your configured port)
    and sign in with an existing sample teacher account username inserted using the `database/data.sql` SQL script at step 2 and the password `teacher@123!`.