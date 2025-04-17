package ru.hogwarts.school.model;

public class Student {
    private Long id;
    private String name;
    private int age;
    private String house;       // Поле факультет (Гриффиндор, Слизерин и т.д.)
    private String pet;         // Поле питомец (сова, кот, жаба)

    public Student(Long id, String name, int age, String house, String pet) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.house = house;
        this.pet = pet;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }
}