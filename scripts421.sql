-- а. Возраст студента не может быть меньше 16 лет
ALTER TABLE student ADD CONSTRAINT age_check CHECK (age >= 16);

-- б. Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE student ALTER COLUMN name SET NOT NULL;
ALTER TABLE student ADD CONSTRAINT name_unique UNIQUE (name);

-- в. Пара "значение названия" - "цвет факультета" должна быть уникальной
ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color);

-- г. При создании студента без возраста ему автоматически должно присваиваться 20 лет
ALTER TABLE student ALTER COLUMN age SET DEFAULT 20;