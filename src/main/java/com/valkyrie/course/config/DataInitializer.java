package com.valkyrie.course.config;

import com.valkyrie.course.model.*;
import com.valkyrie.course.repository.CourseRepository;
import com.valkyrie.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据，避免重复初始化
        if (courseRepository.count() > 0) {
            System.out.println("数据已存在，跳过初始化");
            return;
        }

        // 创建测试课程
        Instructor instructor1 = new Instructor("T001", "张教授", "zhang@example.edu.cn");
        ScheduleSlot schedule1 = new ScheduleSlot("MONDAY", "08:00", "10:00", 50);
        Course course1 = new Course();
        course1.setId("C001");
        course1.setCode("CS101");
        course1.setTitle("计算机科学导论");
        course1.setInstructor(instructor1);
        course1.setSchedule(schedule1);
        course1.setCapacity(60);
        course1.setEnrolled(0);
        courseRepository.save(course1);

        Instructor instructor2 = new Instructor("T002", "李教授", "li@example.edu.cn");
        ScheduleSlot schedule2 = new ScheduleSlot("TUESDAY", "10:00", "12:00", 30);
        Course course2 = new Course();
        course2.setId("C002");
        course2.setCode("CS201");
        course2.setTitle("数据结构");
        course2.setInstructor(instructor2);
        course2.setSchedule(schedule2);
        course2.setCapacity(40);
        course2.setEnrolled(0);
        courseRepository.save(course2);

        Instructor instructor3 = new Instructor("T003", "王教授", "wang@example.edu.cn");
        ScheduleSlot schedule3 = new ScheduleSlot("WEDNESDAY", "14:00", "16:00", 25);
        Course course3 = new Course();
        course3.setId("C003");
        course3.setCode("CS301");
        course3.setTitle("算法分析");
        course3.setInstructor(instructor3);
        course3.setSchedule(schedule3);
        course3.setCapacity(30);
        course3.setEnrolled(0);
        courseRepository.save(course3);

        // 创建测试学生
        Student student1 = new Student();
        student1.setId("S001");
        student1.setStudentId("2024001");
        student1.setName("张三");
        student1.setMajor("计算机科学与技术");
        student1.setGrade(2024);
        student1.setEmail("zhangsan@example.com");
        student1.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setId("S002");
        student2.setStudentId("2024002");
        student2.setName("李四");
        student2.setMajor("软件工程");
        student2.setGrade(2024);
        student2.setEmail("lisi@example.com");
        student2.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student2);

        Student student3 = new Student();
        student3.setId("S003");
        student3.setStudentId("2024003");
        student3.setName("王五");
        student3.setMajor("信息安全");
        student3.setGrade(2024);
        student3.setEmail("wangwu@example.com");
        student3.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student3);

        Student student4 = new Student();
        student4.setId("S004");
        student4.setStudentId("2024004");
        student4.setName("赵六");
        student4.setMajor("人工智能");
        student4.setGrade(2024);
        student4.setEmail("zhaoliu@example.com");
        student4.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student4);

        Student student5 = new Student();
        student5.setId("S005");
        student5.setStudentId("2024005");
        student5.setName("钱七");
        student5.setMajor("大数据技术");
        student5.setGrade(2024);
        student5.setEmail("qianqi@example.com");
        student5.setCreatedAt(LocalDateTime.now());
        studentRepository.save(student5);

        System.out.println("测试数据初始化完成！");
        System.out.println("创建了3门课程和5名学生");
    }
}