package edu.ccrm.domain;

public class Instructor extends Person {
    private String department;

    public Instructor(String id, String fullName, String email, String department){
        super(id, fullName, email);
        this.department = department;
    }

    @Override public String getRole(){ return "Instructor"; }
    public String getDepartment(){ return department; }
    public void setDepartment(String dept){ this.department = dept; }

    @Override public String toString(){
        return super.toString() + String.format("[dept=%s]", department);
    }
}
