package ru.hogwarts.school.model;

public class Faculty {
    private Long id;
    private String name;
    private String color;
    private String founder;     // Поле 1: основатель факультета
    private int studentsCount;  // Поле 2: количество студентов

    public Faculty(Long id, String name, String color, String founder, int studentsCount) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.founder = founder;
        this.studentsCount = studentsCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }
}