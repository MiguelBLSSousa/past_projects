package nl.utwente.di.notebridge.model;

public class StudentNotification {
    private int sid;
    private String message;
    private int from;
    private String name;

    public StudentNotification(){
    }

    public StudentNotification(int sid, String message, int from) {
        this.sid = sid;
        this.message = message;
        this.from = from;
    }

    public StudentNotification(String message, String name){
        this.message = message;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
