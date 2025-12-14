package com.valkyrie.user.service;

import com.valkyrie.user.model.Student;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {
    
    private final ConcurrentHashMap<String, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @PostConstruct
    public void init() {
        // 初始化一些测试数据
        Student student1 = new Student();
        student1.setId("1");
        student1.setStudentId("S001");
        student1.setName("张三");
        student1.setMajor("计算机科学与技术");
        student1.setGrade(3);
        student1.setEmail("zhangsan@example.com");
        students.put("1", student1);
        
        Student student2 = new Student();
        student2.setId("2");
        student2.setStudentId("S002");
        student2.setName("李四");
        student2.setMajor("软件工程");
        student2.setGrade(2);
        student2.setEmail("lisi@example.com");
        students.put("2", student2);
        
        idGenerator.set(3L);
    }
    
    public Student findById(String id) {
        return students.get(id);
    }
    
    public Student save(Student student) {
        if (student.getId() == null) {
            student.setId(String.valueOf(idGenerator.getAndIncrement()));
        }
        students.put(student.getId(), student);
        return student;
    }
    
    public Student update(String id, Student studentDetails) {
        Student student = students.get(id);
        if (student == null) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        
        student.setStudentId(studentDetails.getStudentId());
        student.setName(studentDetails.getName());
        student.setMajor(studentDetails.getMajor());
        student.setGrade(studentDetails.getGrade());
        student.setEmail(studentDetails.getEmail());
        
        students.put(id, student);
        return student;
    }
    
    public void deleteById(String id) {
        students.remove(id);
    }
}