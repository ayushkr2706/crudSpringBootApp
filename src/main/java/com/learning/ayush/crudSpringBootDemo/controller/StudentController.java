package com.learning.ayush.crudSpringBootDemo.controller;

import com.learning.ayush.crudSpringBootDemo.dto.*;
import com.learning.ayush.crudSpringBootDemo.entity.Student;
import com.learning.ayush.crudSpringBootDemo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService){

        this.studentService = studentService;
    }

    //Create Student
    @PostMapping("/create")
    public ResponseEntity<createRespDTO> createStudent(@Valid @RequestBody createReqDTO student){
       // System.out.println("Inside StudentController");
        createRespDTO createdStudent = studentService.createStudent(student);
        //System.out.println("Exiting StudentController");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    //Read one Student
    @GetMapping("/get/{id}")
    public ResponseEntity<getRespDTO> getStudent(@PathVariable Long id){
        getRespDTO fetchedStudent = studentService.getStudent(id);

//        if(fetchedStudent == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchedStudent);
    }

    //Read All the Students
    @GetMapping("/getAll")
    public ResponseEntity<List<getRespDTO>> getStudent(){
        List<getRespDTO> fetchedStudents = studentService.getAllStudents();

//        if(fetchedStudents == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchedStudents);
    }

    //Update Student
    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateRespDTO> updateStudent(@PathVariable Long id,
                                                 @RequestBody UpdateReqDTO student){

        UpdateRespDTO updatedStudent = studentService.updateStudent(id, student);

//        if(updatedStudent == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedStudent);
    }

    //Delete Student
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){

        studentService.deleteStudent(id);
//        if(!isDeleted){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not deleted");
//        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    //Soft Delete
    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<String> deleteStudentSoftly(@PathVariable Long id){


         studentService.deleteStudentSoftly(id);

//        if(!isDeleted){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record doesn't exist");
//        }

        return ResponseEntity.status(HttpStatus.OK).body("Record Deleted successfully");
    }

}
