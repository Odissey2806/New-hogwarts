package ru.hogwarts.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Avatar avatar;

    public Student() {
    }

    public Student(Long id, String name, int age, String pet, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.pet = pet;
        this.faculty = faculty;
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

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
        if (avatar != null && avatar.getStudent() != this) {
            avatar.setStudent(this);
        }
    }
}
