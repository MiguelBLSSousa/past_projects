package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class StudentProfile {

    private int sid;
    private String firstname;
    private String surname;
    private String city;
    private List<String> instruments;
    private List<Lesson> lessons;
    private String picture;

    public StudentProfile() {

    }

    public StudentProfile(int sid, String firstname, String surname, String city, List<String> instruments, List<Lesson> lessons, String picture) {
        this.sid = sid;
        this.firstname = firstname;
        this.surname = surname;
        this.city = city;
        this.instruments = instruments;
        this.lessons = lessons;
        this.picture = picture;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
