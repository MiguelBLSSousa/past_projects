package exceptions;

import com.sun.jdi.event.ExceptionEvent;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message){
        super(message);
    }
}
