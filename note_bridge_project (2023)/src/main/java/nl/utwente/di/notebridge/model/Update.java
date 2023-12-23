package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Update {
    private int pid;
    private int tid;
    private int sid;
    private String firstname;
    private String surname;
    private String phoneNum;
    private String email;
    private String oldPostalCode;
    private int oldHouseNum;
    private String street;
    private int newHouseNum;
    private String city;
    private String newPostalCode;
    private String country;
    private String instruments;

    public Update() {

    }

    public Update(int pid, int tid, int sid, String firstname, String surname, String phoneNum, String email, String oldPostalCode, int oldHouseNum, String street, int newHouseNum, String city, String newPostalCode, String country, String instruments) {
        this.pid = pid;
        this.tid = tid;
        this.sid = sid;
        this.firstname = firstname;
        this.surname = surname;
        this.phoneNum = phoneNum;
        this.email = email;
        this.oldPostalCode = oldPostalCode;
        this.oldHouseNum = oldHouseNum;
        this.street = street;
        this.newHouseNum = newHouseNum;
        this.city = city;
        this.newPostalCode = newPostalCode;
        this.country = country;
        this.instruments = instruments;
    }

    public int getPid() {
        return pid;
    }

    public int getTid() {
        return tid;
    }

    public int getSid() {
        return sid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getStreet() {
        return street;
    }

    public int getOldHouseNum() {
        return oldHouseNum;
    }

    public int getNewHouseNum() {
        return newHouseNum;
    }

    public String getCity() {
        return city;
    }

    public String getOldPostalCode() {
        return oldPostalCode;
    }

    public String getNewPostalCode() {
        return newPostalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getInstruments() {
        return instruments;
    }
}
