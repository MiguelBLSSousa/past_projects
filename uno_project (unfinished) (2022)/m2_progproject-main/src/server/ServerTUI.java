package server;

import exceptions.InvalidInputException;
import util.TextIO;

import java.io.PrintWriter;


/**
 * Hotel Server TUI for user input and user messages
 *
 * @author Wim Kamerman
 */
public class ServerTUI {

    /** The PrintWriter to write messages to */
    private PrintWriter console;

    /**
     * Constructs a new ServerTUI. Initializes the console.
     */
    public ServerTUI() {
        console = new PrintWriter(System.out, true);
    }

    public void showMessage(String message) {
        console.println(message);
    }

    public boolean getBoolean(String question) {
        console.println(question);
        String input = TextIO.getlnWord();
        if (input.equals("YES")) {
            return true;
        } else if (input.equals("NO")){
            return false;
        }
        else {
            new InvalidInputException("Invalid Input");
            return false;
        }
    }

    public int getInt(String question) {
        console.println(question);
        int input = TextIO.getInt();
        return input;
    }
}
