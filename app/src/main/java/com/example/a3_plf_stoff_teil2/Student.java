package com.example.a3_plf_stoff_teil2;

import java.util.Date;
import java.util.UUID;

public class Student {
    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", numberOfSubjects=" + numberOfSubjects +
                ", joinedSchool=" + joinedSchool +
                ", schoolName='" + schoolName + '\'' +
                ", graduated=" + graduated +
                ", id='" + id + '\'' +
                '}';
    }

    private int age;
    private String name;
    private int numberOfSubjects;
    private Date joinedSchool;
    private String schoolName;
    private boolean graduated;
    private String id;

    private Student(final Builder builder){
        this.age = builder.getAge();
        this.name = builder.getName();
        this.numberOfSubjects = builder.getNumberOfSubjects();
        this.joinedSchool = builder.getJoinedSchool();
        this.schoolName = builder.getSchoolName();
        this.graduated = builder.isGraduated();
        this.id = builder.getId();
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfSubjects(int numberOfSubjects) {
        this.numberOfSubjects = numberOfSubjects;
    }

    public void setJoinedSchool(Date joinedSchool) {
        this.joinedSchool = joinedSchool;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSubjects() {
        return numberOfSubjects;
    }

    public Date getJoinedSchool() {
        return joinedSchool;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public String getId() {
        return id;
    }

    public static class Builder{
        private int age;
        private String name;
        private int numberOfSubjects;
        private Date joinedSchool;
        private String schoolName;
        private boolean graduated;
        private String id;

        public Builder generateId(){
            id = UUID.randomUUID().toString();
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public String getId() {
            return id;
        }

        public int getAge() {
            return age;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public int getNumberOfSubjects() {
            return numberOfSubjects;
        }

        public Builder setNumberOfSubjects(int numberOfSubjects) {
            this.numberOfSubjects = numberOfSubjects;
            return this;
        }

        public Date getJoinedSchool() {
            return joinedSchool;
        }

        public Builder setJoinedSchool(Date joinedSchool) {
            this.joinedSchool = joinedSchool;
            return this;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public Builder setSchoolName(String schoolName) {
            this.schoolName = schoolName;
            return this;
        }

        public boolean isGraduated() {
            return graduated;
        }

        public Builder setGraduated(boolean graduated) {
            this.graduated = graduated;
            return this;
        }

        public Student create(){
            return new Student(this);
        }
    }
}


