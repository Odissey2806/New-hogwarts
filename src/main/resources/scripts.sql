1. Вывести всех студентов
SELECT * FROM student;

2. Найти всех студентов старше 18 лет
SELECT * FROM student WHERE age > 18;

3. Найти факультет по цвету
SELECT * FROM faculty WHERE LOWER(color) = LOWER('red');

4. Найти факультет по имени
SELECT * FROM faculty WHERE LOWER(name) = LOWER('gryffindor');

5. Получить всех студентов факультета с id = 1
SELECT * FROM student WHERE faculty_id = 1;

6. Получить всех студентов определённого возраста
SELECT * FROM student WHERE age = 18;

7. Получить все факультеты определенного цвета
SELECT * FROM faculty WHERE LOWER(color) = LOWER('green');

8. Обновить питомца у студента
UPDATE student SET pet = 'сова' WHERE id = 1;

9. Удалить факультет по id
DELETE FROM faculty WHERE id = 5;

10. Посчитать количество студентов в факультете
SELECT COUNT(*) FROM student WHERE faculty_id = 2;

11. Найти студента с самым младшим возрастом
SELECT * FROM student ORDER BY age ASC LIMIT 1;

12. Найти факультет, к которому принадлежит конкретный студент
SELECT faculty.* FROM faculty
JOIN student ON faculty.id = student.faculty_id
WHERE student.id = 3;

13. Получить список студентов с возрастом в диапазоне
SELECT * FROM student WHERE age BETWEEN 18 AND 25;

14. Вывести факультеты и количество студентов на каждом
SELECT faculty.name, COUNT(student.id) AS students_count
FROM faculty
LEFT JOIN student ON faculty.id = student.faculty_id
GROUP BY faculty.name;

15. Найти факультеты без студентов
SELECT * FROM faculty
WHERE id NOT IN (SELECT DISTINCT faculty_id FROM student WHERE faculty_id IS NOT NULL);