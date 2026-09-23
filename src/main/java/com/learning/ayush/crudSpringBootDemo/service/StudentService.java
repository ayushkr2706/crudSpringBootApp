package com.learning.ayush.crudSpringBootDemo.service;

import com.learning.ayush.crudSpringBootDemo.dto.createReqDTO;
import com.learning.ayush.crudSpringBootDemo.dto.createRespDTO;
import com.learning.ayush.crudSpringBootDemo.dto.getRespDTO;
import com.learning.ayush.crudSpringBootDemo.entity.Student;
import com.learning.ayush.crudSpringBootDemo.exception.DuplicateResourceException;
import com.learning.ayush.crudSpringBootDemo.exception.ResourceNotFoundException;
import com.learning.ayush.crudSpringBootDemo.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository repository;

    public StudentService(StudentRepository repository){
        this.repository = repository;
    }

    public createRespDTO createStudent(createReqDTO studentReq){

//        studentReq.setDeleted(false);
        Student student = mapToEntity(studentReq);

        if(emailExists(student)){
            throw new DuplicateResourceException("Email is already registered.");
        }

        Student studentResponse = repository.save(student);
    //   System.out.println("Exiting StudentSerivce");
        return mapToDTO(studentResponse);
    }

    public getRespDTO getStudent(Long id){

        /* Optional<Student> fetchedStudent = repository.findByIdAndIsDeletedFalse(id);

            if(fetchedStudent.isPresent()){
                return mapToGetResponse(fetchedStudent.get());
            }
            else return null;

            return mapToGetResponse(fetchedStudent.get());
         */

        Student fetchedStudent = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " does not exist."));

        return mapToGetResponse(fetchedStudent);
    }

    public List<getRespDTO> getAllStudents(){
        List<Student> students = repository.findByAndIsDeletedFalse();
        return mapToGetAllDTO(students);

    }

    public Student updateStudent(Long id, Student student){
        Optional<Student> existingStudent = repository.findByIdAndIsDeletedFalse(id);
        if(existingStudent.isEmpty()){
            return null;
        }

        Student studentToUpdate = existingStudent.get();
        studentToUpdate.setName(student.getName());
        studentToUpdate.setEmail(student.getEmail());
        studentToUpdate.setRollNo(student.getRollNo());
        studentToUpdate.setAge(student.getAge());
        studentToUpdate.setSubject(student.getSubject());

        return repository.save(studentToUpdate);

    }

    public Boolean deleteStudent(Long id){

        Boolean doesExist = repository.existsById(id);

        if(!doesExist){
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    public Boolean deleteStudentSoftly(Long id){

        Optional <Student> existingStudent = repository.findByIdAndIsDeletedFalse(id);

        if(existingStudent.isEmpty()){
            return false;
        }

        Student studentToSave = existingStudent.get();

        studentToSave.setDeleted(true);
        repository.save(studentToSave);
        return true;

    }

    private Student mapToEntity(createReqDTO req){

        Student student = new Student();

        student.setName(req.getName());
        student.setAge(req.getAge());
        student.setEmail(req.getEmail());
        student.setRollNo(req.getRollNo());
        student.setSubject(req.getSubject());
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        student.setDeleted(false);
        return student;
    }

    private createRespDTO mapToDTO(Student student){

        createRespDTO response = new createRespDTO();
        response.setId(student.getId());
        response.setName(student.getName());
        response.setAge(student.getAge());
        response.setEmail(student.getEmail());
        response.setRollNo(student.getRollNo());
        response.setSubject(student.getSubject());
        response.setCreatedAt(student.getCreatedAt());
        response.setUpdatedAt(student.getUpdatedAt());
        response.setMessage("Student saved successfully");
        return response;
    }

    private getRespDTO mapToGetResponse(Student student){

        getRespDTO response = new getRespDTO();

        response.setName(student.getName());
        response.setAge(student.getAge());
        response.setRollNo(student.getRollNo());
        response.setSubject(student.getSubject());
        response.setMessage("Student Fetched Successfully");
        return response;

    }

    private Boolean emailExists(Student student){

        String email = student.getEmail();

        if(repository.existsByEmail(email)){
            return true;
        }
        return false;
    }

    private List<getRespDTO> mapToGetAllDTO(List<Student> students){

        List<getRespDTO> allStudents = new ArrayList<>();
        int i = 1;

        for(Student s : students){

            getRespDTO studentToAdd = new getRespDTO();
            studentToAdd.setName(s.getName());
            studentToAdd.setAge(s.getAge());
            studentToAdd.setRollNo(s.getRollNo());
            studentToAdd.setSubject(s.getSubject());
            studentToAdd.setMessage("Student " + i);
            i++;
            allStudents.add(studentToAdd);
        }

        return allStudents;
    }
}
