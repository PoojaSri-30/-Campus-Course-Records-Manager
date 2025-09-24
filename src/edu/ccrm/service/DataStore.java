package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, List<Enrollment>> enrollments = new ConcurrentHashMap<>();

    private DataStore(){}

    public static DataStore getInstance(){ return INSTANCE; }

    // Students
    public void addStudent(Student s){ students.put(s.getId(), s); }
    public Optional<Student> getStudentById(String id){ return Optional.ofNullable(students.get(id)); }
    public Optional<Student> findStudentByRegNo(String regNo) {
        return students.values().stream()
                .filter(s -> s.getRegNo().equalsIgnoreCase(regNo))
                .findFirst();
    }
    public Collection<Student> listStudents(){ return students.values(); }
    public int studentCount(){ return students.size(); }

    // Courses
    public void addCourse(Course c){ courses.put(c.getCode().getCode(), c); }
    public Optional<Course> getCourseByCode(String code){ return Optional.ofNullable(courses.get(code.toUpperCase())); }
    public Collection<Course> listCourses(){ return courses.values(); }
    public int courseCount(){ return courses.size(); }

    // Enrollments
    public void addEnrollment(Enrollment e) throws DuplicateEnrollmentException {
        List<Enrollment> list = enrollments.computeIfAbsent(e.getStudentId(), k -> new ArrayList<>());
        boolean duplicate = list.stream().anyMatch(en -> en.getCourseCode().equals(e.getCourseCode()));
        if (duplicate) throw new DuplicateEnrollmentException("Student already enrolled in " + e.getCourseCode());
        list.add(e);
    }

    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollments.getOrDefault(studentId, Collections.emptyList());
    }

    public Map<String, List<Enrollment>> allEnrollments(){ 
        return Collections.unmodifiableMap(enrollments); 
    }
}
