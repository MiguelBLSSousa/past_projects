package server;

import exceptions.InvalidInputException;
import util.TextIO;

import java.io.PrintWriter;


/**
 * Hotel Server TUI for user input and user messages
 *
 * @author Wim Kamerman
 */
public class ClientTUI {

    /** The PrintWriter to write messages to */
    private PrintWriter console;

    /**
     * Constructs a new ServerTUI. Initializes the console.
     */
    public ClientTUI() {
        console = new PrintWriter(System.out, true);
    }

    public void showMessage(String message) {
        console.println(message);
    }

    public boolean getBoolean(String question) throws InvalidInputException{
        console.println(question);
        String input = TextIO.getlnWord();
        if (input.equals("YES")) {
            return true;
        } else if (input.equals("NO")){
            return false;
        }
        else {
            throw new InvalidInputException("Invalid Input");
        }
    }

    public int getInt(String question) {
        console.println(question);
        int input = TextIO.getInt();
        return input;
    }
}
