package com.learning.ayush.crudSpringBootDemo.dto;

import jakarta.validation.constraints.*;

public class createReqDTO {

    @Size(min = 2, max = 50, message = "Name must be between 2 to 50 characters long.")
    @NotBlank(message = "Name cannot be empty, blank or null")
    private String name;

    @NotBlank(message = "Email cannot be empty, blank or null")
    @Email(message = "Wrong email format")
    private String email;

    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be greater than or equal to 18")
    private int age;

    @NotNull(message = "Roll No cannot be blank")
    private int rollNo;
    @NotBlank
    private String subject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
