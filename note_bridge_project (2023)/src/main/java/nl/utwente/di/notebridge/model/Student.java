package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Student extends Person {
    private int sid;

    public Student(int id, String firstName, String lastName, int phoneNumber, String email, int sid) {
        super(id, firstName, lastName, phoneNumber, email);
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
