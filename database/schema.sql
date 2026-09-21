-- Create database
CREATE DATABASE IF NOT EXISTS activity_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE activity_manager;

-- Users
CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  user_password VARCHAR(255) NOT NULL,
  user_role ENUM('STUDENT', 'TEACHER', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
  user_email VARCHAR(50) UNIQUE
);

-- Departments
CREATE TABLE departments (
  department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  department_name VARCHAR(50) NOT NULL UNIQUE
);

-- Student Groups
CREATE TABLE student_groups (
  group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL UNIQUE
);

-- Teachers
CREATE TABLE teachers (
  teacher_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  last_name VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  department_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL UNIQUE,
  FOREIGN KEY (department_id) REFERENCES Departments(department_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Students
CREATE TABLE students (
  student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  last_name VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  group_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL UNIQUE,
  FOREIGN KEY (group_id) REFERENCES Student_Groups(group_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Courses
CREATE TABLE courses (
  course_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_name VARCHAR(50) NOT NULL UNIQUE,
  course_type ENUM('MANDATORY', 'OPTIONAL', 'ELECTIVE') DEFAULT 'MANDATORY',
  teacher_id BIGINT NOT NULL,
  FOREIGN KEY (teacher_id) REFERENCES Teachers(teacher_id)
);

-- Course Sessions
CREATE TABLE course_sessions (
  session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT NOT NULL,
  session_date DATE NOT NULL,
  session_time TIME NOT NULL,
  duration TIME DEFAULT '02:00:00',
  FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

-- Course Enrollment
CREATE TABLE course_enrollment (
  student_id BIGINT NOT NULL,
  course_id BIGINT NOT NULL,
  enrollment_type ENUM('ACTIVE', 'REPETITION') DEFAULT 'ACTIVE',
  PRIMARY KEY (student_id, course_id),
  FOREIGN KEY (student_id) REFERENCES Students(student_id),
  FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

-- Attendance
CREATE TABLE attendance (
  session_id BIGINT NOT NULL,
  student_id BIGINT NOT NULL,
  attendance_status ENUM('PRESENT', 'ABSENT', 'EXCUSED') DEFAULT 'ABSENT',
  PRIMARY KEY (session_id, student_id),
  FOREIGN KEY (session_id) REFERENCES Course_Sessions(session_id),
  FOREIGN KEY (student_id) REFERENCES Students(student_id)
);

-- Activities
CREATE TABLE activities (
  activity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  teacher_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  start_date DATE,
  end_date DATE NOT NULL,
  status ENUM(
    'IN_PROGRESS',
    'SUSPENDED',
    'RESUMED',
    'REPEATED',
    'COMPLETED'
  ) NOT NULL DEFAULT 'IN_PROGRESS',
  priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL DEFAULT 'LOW',
  difficulty ENUM('EASY', 'MEDIUM', 'HARD') NOT NULL DEFAULT 'EASY',
  FOREIGN KEY (teacher_id) REFERENCES Teachers(teacher_id)
);

-- Activity Subtasks
CREATE TABLE activity_subtasks (
  subtask_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  activity_id BIGINT NOT NULL,
  title VARCHAR(100) NOT NULL,
  is_completed BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (activity_id) REFERENCES Activities(activity_id)
);