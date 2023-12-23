package server;

public class Exit extends Exception {

    String msg;
    Exit(String m){
        this.msg = m;
    }
    @Override
    public String getMessage() {
        return msg;
    }
}
