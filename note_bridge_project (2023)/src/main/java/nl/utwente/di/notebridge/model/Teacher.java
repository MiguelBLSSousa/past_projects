package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Teacher extends Person {
    private int tid;
    private int rating;

    public Teacher(int id, String firstName, String lastName, int phoneNumber, String email, int tid) {
        super(id, firstName, lastName, phoneNumber, email);
        this.tid = tid;
        this.rating = 0;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
