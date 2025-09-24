package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Enrollment {
    private final String studentId;
    private final CourseCode courseCode;
    private final LocalDate enrolledAt;
    private Double marks;

    public Enrollment(String studentId, CourseCode courseCode) {
        this.studentId = Objects.requireNonNull(studentId);
        this.courseCode = Objects.requireNonNull(courseCode);
        this.enrolledAt = LocalDate.now();
    }

    public String getStudentId(){ return studentId; }
    public CourseCode getCourseCode(){ return courseCode; }
    public LocalDate getEnrolledAt(){ return enrolledAt; }
    public Optional<Double> getMarks(){ return Optional.ofNullable(marks); }
    public void setMarks(double marks){ this.marks = marks; }

    public Grade computeGrade(){
        if (marks == null) return Grade.F;
        if (marks >= 90) return Grade.S;
        if (marks >= 80) return Grade.A;
        if (marks >= 70) return Grade.B;
        if (marks >= 60) return Grade.C;
        if (marks >= 50) return Grade.D;
        if (marks >= 40) return Grade.E;
        return Grade.F;
    }

    @Override public String toString(){
        return String.format("Enrollment[student=%s,course=%s,marks=%s,grade=%s]",
                studentId, courseCode, marks==null?"n/a":marks, marks==null?"n/a":computeGrade());
    }
}
