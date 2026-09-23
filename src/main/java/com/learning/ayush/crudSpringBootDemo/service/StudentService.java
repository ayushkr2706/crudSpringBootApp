package com.learning.ayush.crudSpringBootDemo.service;

import com.learning.ayush.crudSpringBootDemo.dto.*;
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
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " does not exist."));

        return mapToGetResponse(fetchedStudent);
    }

    public List<getRespDTO> getAllStudents(){
        List<Student> students = repository.findByAndIsDeletedFalse();
        return mapToGetAllDTO(students);

    }

    public UpdateRespDTO updateStudent(Long id, UpdateReqDTO student){
        //Optional<Student> existingStudent = repository.findByIdAndIsDeletedFalse(id);
        //if(existingStudent.isEmpty()){
          //  return null;

        Student existingStudent = repository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " does not exist."));

        existingStudent.setName(student.getName());
//        existingStudent.setEmail(student.getEmail());
//        existingStudent.setRollNo(student.getRollNo());
        existingStudent.setAge(student.getAge());
        existingStudent.setSubject(student.getSubject());

        repository.save(existingStudent);

        return mapToUpdateResp(existingStudent);
    }

    public void deleteStudent(Long id){

        Student studentToBeDeleted =  repository
                        .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " does not exists."));

        repository.delete(studentToBeDeleted);
    }

    public void deleteStudentSoftly(Long id){

        //Optional <Student> existingStudent = repository.findByIdAndIsDeletedFalse(id);

        Student existingStudent = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " does not exists."));

//        if(existingStudent.isEmpty()){
//            return false;
//        }

//        Student studentToSave = existingStudent.get();

        existingStudent.setDeleted(true);
        repository.save(existingStudent);
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

    private UpdateRespDTO mapToUpdateResp(Student student){

        UpdateRespDTO response = new UpdateRespDTO();

        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setRollNo(student.getRollNo());
        response.setSubject(student.getSubject());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }
}
