package nl.utwente.di.notebridge.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LessonInfo {

     private String starttime;
    private String endtime;
    private String firstname;
    private String surname;
    private int dayofweek;
    private int weekofyear;
    private int year;
    private int lid;
    private int tid;
    private int sid;
    private boolean isPaid;

    public LessonInfo(){}

    public LessonInfo (String starttime, String endtime, String firstname, String surname, int dayofweek, int weekofyear, int year, int lid, int tid, int sid, boolean isPaid){
        this.starttime = starttime;
        this.endtime = endtime;
        this.firstname =firstname;
        this.surname = surname;
        this.dayofweek =dayofweek;
        this.weekofyear = weekofyear;
        this.year = year;
        this.lid = lid;
        this.tid = tid;
        this.sid = sid;
        this.isPaid = isPaid;
    }

    public LessonInfo (String starttime, int dayofweek, int weekofyear){
        this.starttime = starttime;
        this.dayofweek =dayofweek;
        this.weekofyear = weekofyear;

    }


    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
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

    public int getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(int dayofweek) {
        this.dayofweek = dayofweek;
    }

    public int getWeekofyear() {
        return weekofyear;
    }

    public void setWeekofyear(int weekofyear) {
        this.weekofyear = weekofyear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}


