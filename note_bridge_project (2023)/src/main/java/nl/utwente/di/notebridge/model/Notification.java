package nl.utwente.di.notebridge.model;

public class Notification {
    private int pid;
    private String message;

    public Notification(){
    }

    public Notification(int pid, String message) {
        this.pid = pid;
        this.message = message;
    }

    public Notification(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
