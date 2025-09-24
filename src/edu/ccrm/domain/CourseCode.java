package edu.ccrm.domain;

import java.util.Objects;

public final class CourseCode {
    private final String code;
    public CourseCode(String code) {
        this.code = Objects.requireNonNull(code).trim().toUpperCase();
    }
    public String getCode(){ return code; }
    @Override public String toString(){ return code; }
    @Override public boolean equals(Object o){
        return this==o || (o instanceof CourseCode && ((CourseCode)o).code.equals(code));
    }
    @Override public int hashCode(){ return code.hashCode(); }
}
