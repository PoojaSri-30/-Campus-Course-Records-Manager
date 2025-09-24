package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    protected final String id;
    protected String fullName;
    protected String email;
    protected boolean active = true;
    protected final LocalDate createdAt = LocalDate.now();

    public Person(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }

    public abstract String getRole();

    public String getId(){ return id; }
    public String getFullName(){ return fullName; }
    public void setFullName(String fullName){ this.fullName = fullName; }
    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }
    public boolean isActive(){ return active; }
    public void deactivate(){ this.active = false; }
    public LocalDate getCreatedAt(){ return createdAt; }

    @Override public String toString() {
        return String.format("%s[id=%s,name=%s,email=%s,active=%s]", getRole(), id, fullName, email, active);
    }
}
