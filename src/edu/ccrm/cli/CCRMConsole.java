package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.io.FileUtil;
import edu.ccrm.service.DataStore;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class CCRMConsole {
    private final Scanner scanner = new Scanner(System.in);
    private final DataStore ds = DataStore.getInstance();

    public void run(){
        System.out.println("Welcome to CCRM (data dir: " + edu.ccrm.config.AppConfig.getInstance().getDataDir() + ")");
        mainLoop:
        while(true){
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> listStudents();
                case "3" -> addCourse();
                case "4" -> listCourses();
                case "5" -> enrollStudent();
                case "6" -> listEnrollmentsForStudent();
                case "7" -> exportAndBackup();
                case "0" -> { System.out.println("Exiting..."); break mainLoop; }
                default -> System.out.println("Unknown option");
            }
        }
    }

    private void printMenu(){
        System.out.println("\n=== CCRM Menu ===");
        System.out.println("1) Add student");
        System.out.println("2) List students");
        System.out.println("3) Add course");
        System.out.println("4) List courses");
        System.out.println("5) Enroll student in course");
        System.out.println("6) List enrollments for a student");
        System.out.println("7) Export CSVs & Backup");
        System.out.println("0) Exit");
        System.out.print("Choice> ");
    }

    private void addStudent(){
        System.out.print("Full name: "); String name = scanner.nextLine().trim();
        System.out.print("Email: "); String email = scanner.nextLine().trim();
        String id = UUID.randomUUID().toString();
        String regNo = "R" + (1000 + ds.studentCount() + 1);
        Student s = new Student(id, regNo, name, email);
        ds.addStudent(s);
        System.out.println("Added: " + s);
    }

    private void listStudents(){
        ds.listStudents().forEach(System.out::println);
    }

    private void addCourse(){
        System.out.print("Course code (e.g., CS101): "); String code = scanner.nextLine().trim();
        System.out.print("Title: "); String title = scanner.nextLine().trim();
        System.out.print("Credits (int): "); int credits = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Department: "); String dept = scanner.nextLine().trim();
        System.out.print("Semester (SPRING/SUMMER/FALL): "); String sem = scanner.nextLine().trim().toUpperCase();
        System.out.print("Instructor name: "); String iname = scanner.nextLine().trim();
        System.out.print("Instructor email: "); String iemail = scanner.nextLine().trim();

        Instructor i = new Instructor(UUID.randomUUID().toString(), iname, iemail, dept);
        Course c = new Course.Builder()
                .code(code).title(title).credits(credits).department(dept)
                .semester(Semester.valueOf(sem)).instructor(i).build();
        ds.addCourse(c);
        System.out.println("Added course: " + c);
    }

    private void listCourses(){
        ds.listCourses().forEach(System.out::println);
    }

    private void enrollStudent(){
        System.out.print("Student regNo (e.g., R1001): "); String reg = scanner.nextLine().trim();
        Optional<Student> maybe = ds.findStudentByRegNo(reg);
        if (maybe.isEmpty()) { System.out.println("Student not found"); return; }
        Student s = maybe.get();

        System.out.print("Course code: "); String code = scanner.nextLine().trim();
        Optional<Course> mc = ds.getCourseByCode(code);
        if (mc.isEmpty()){ System.out.println("Course not found"); return; }
        Course course = mc.get();

        Enrollment e = new Enrollment(s.getId(), course.getCode());
        try {
            ds.addEnrollment(e);
            s.getEnrolledCourses().add(course);
            System.out.println("Enrolled " + s.getRegNo() + " to " + course.getCode());
        } catch (DuplicateEnrollmentException ex) {
            System.out.println("Cannot enroll: " + ex.getMessage());
        }
    }

    private void listEnrollmentsForStudent(){
        System.out.print("Student regNo: "); String reg = scanner.nextLine().trim();
        Optional<Student> maybe = ds.findStudentByRegNo(reg);
        if (maybe.isEmpty()){ System.out.println("Student not found"); return; }
        Student s = maybe.get();
        ds.getEnrollmentsForStudent(s.getId()).forEach(System.out::println);
    }

    private void exportAndBackup(){
        try {
            Path exportedDir = FileUtil.exportAllCsv(ds);
            Path backup = FileUtil.backupCsvs(exportedDir);
            System.out.println("Exported CSVs to: " + exportedDir);
            System.out.println("Backup created at: " + backup);
        } catch (Exception ex) {
            System.err.println("Export/backup failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
