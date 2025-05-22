-- Создаем таблицу машин
CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    cost NUMERIC(12, 2) NOT NULL CHECK (cost > 0)
);

-- Создаем таблицу людей
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL CHECK (age > 0),
    has_license BOOLEAN NOT NULL DEFAULT FALSE,
    car_id BIGINT REFERENCES car(id)
);

-- Комментарии к таблицам
COMMENT ON TABLE car IS 'Таблица для хранения информации о машинах';
COMMENT ON COLUMN car.brand IS 'Марка машины';
COMMENT ON COLUMN car.model IS 'Модель машины';
COMMENT ON COLUMN car.cost IS 'Стоимость машины';

COMMENT ON TABLE person IS 'Таблица для хранения информации о людях';
COMMENT ON COLUMN person.name IS 'Имя человека';
COMMENT ON COLUMN person.age IS 'Возраст человека';
COMMENT ON COLUMN person.has_license IS 'Наличие водительских прав';
COMMENT ON COLUMN person.car_id IS 'Ссылка на машину, которой пользуется человек';