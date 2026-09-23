package com.learning.ayush.crudSpringBootDemo.repository;

import com.learning.ayush.crudSpringBootDemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByIdAndIsDeletedFalse(Long id);

    List<Student> findByAndIsDeletedFalse();

    Boolean existsByEmail(String email);
}
