package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private final CourseCode code;
    private final String title;
    private final int credits;
    private final Instructor instructor;
    private final Semester semester;
    private final String department;
    private boolean active = true;

    private Course(Builder b) {
        this.code = b.code;
        this.title = b.title;
        this.credits = b.credits;
        this.instructor = b.instructor;
        this.semester = b.semester;
        this.department = b.department;
    }

    public CourseCode getCode(){ return code; }
    public String getTitle(){ return title; }
    public int getCredits(){ return credits; }
    public Instructor getInstructor(){ return instructor; }
    public Semester getSemester(){ return semester; }
    public String getDepartment(){ return department; }
    public boolean isActive(){ return active; }
    public void deactivate(){ active = false; }

    @Override public String toString(){
        return String.format("%s - %s (%d credits) [%s] by %s", code, title, credits, department, 
            instructor == null ? "TBA" : instructor.getFullName());
    }

    public static class Builder {
        private CourseCode code;
        private String title;
        private int credits;
        private Instructor instructor;
        private Semester semester;
        private String department;

        public Builder code(String c){ this.code = new CourseCode(c); return this; }
        public Builder title(String t){ this.title = t; return this; }
        public Builder credits(int c){ this.credits = c; return this; }
        public Builder instructor(Instructor i){ this.instructor = i; return this; }
        public Builder semester(Semester s){ this.semester = s; return this; }
        public Builder department(String d){ this.department = d; return this; }

        public Course build(){
            Objects.requireNonNull(code,"code required");
            Objects.requireNonNull(title,"title required");
            if(credits <= 0) throw new IllegalArgumentException("credits must be > 0");
            return new Course(this);
        }
    }
}
