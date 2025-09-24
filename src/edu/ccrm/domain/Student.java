package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Student extends Person {
    private final String regNo;
    private final LocalDate enrollmentDate;
    private final Set<Course> enrolledCourses = new HashSet<>();

    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.enrollmentDate = LocalDate.now();
    }

    @Override public String getRole(){ return "Student"; }
    public String getRegNo(){ return regNo; }
    public LocalDate getEnrollmentDate(){ return enrollmentDate; }
    public Set<Course> getEnrolledCourses(){ return enrolledCourses; }

    public class Profile {
        public String summary(){
            return String.format("%s (%s) - enrolled courses: %d", fullName, regNo, enrolledCourses.size());
        }
    }

    @Override public String toString(){
        return super.toString() + String.format("[regNo=%s,enrolled=%d]", regNo, enrolledCourses.size());
    }
}
