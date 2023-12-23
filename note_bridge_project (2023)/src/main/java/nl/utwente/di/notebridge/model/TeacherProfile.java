package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class TeacherProfile {
    private int tid;
    private String firstname;
    private String surname;
    private String city;
    private String zip;
    private double rating;
    private int rate;
    private List<String> instruments;
    private List<Availability> availabilities;
    private String picture;
    private String video;

    public TeacherProfile() {

    }

    public TeacherProfile(int tid, String firstname, String surname, String city, String zip, double rating, int rate, List<String> instruments, List<Availability> availabilities, String picture, String video) {
        this.tid = tid;
        this.firstname = firstname;
        this.surname = surname;
        this.city = city;
        this.zip = zip;
        this.rating = rating;
        this.rate = rate;
        this.instruments = instruments;
        this.availabilities = availabilities;
        this.picture = picture;
        this.video = video;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
    }

    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
