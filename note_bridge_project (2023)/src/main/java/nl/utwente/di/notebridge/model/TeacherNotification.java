package nl.utwente.di.notebridge.model;

public class TeacherNotification {
    private int tid;
    private String message;
    private int from;
    private String name;

    public TeacherNotification(){
    }

    public TeacherNotification(int tid, String message, int from) {
        this.tid = tid;
        this.message = message;
        this.from = from;
    }

    public TeacherNotification(String message, String name){
        this.message = message;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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
