-- Optional sample data so you can try the reports quickly.
USE student_management;

INSERT INTO students (name, email, phone) VALUES
    ('Aarav Sharma', 'aarav@example.com', '9800000001'),
    ('Sita Thapa',   'sita@example.com',  '9800000002'),
    ('Ram Karki',    'ram@example.com',   '9800000003');

INSERT INTO courses (course_code, course_name, credit_hours) VALUES
    ('JAVA101', 'Java Programming', 3),
    ('DBMS201', 'Database Systems', 3);

INSERT INTO enrollments (student_id, course_id, marks) VALUES
    (1, 1, 88.50),
    (1, 2, 76.00),
    (2, 1, 92.00),
    (2, 2, 81.50),
    (3, 1, 67.00);
