package server;

import exceptions.InvalidInputException;
import org.w3c.dom.Text;
import util.TextIO;

import java.net.*;
import java.util.Scanner;
import java.io.*;

/**
 * This program opens a connection to a computer specified
 * as the first command-line argument.  If no command-line
 * argument is given, it prompts the user for a computer
 * to connect to.  The connection is made to
 * the port specified by LISTENING_PORT.  The program reads one
 * line of text from the connection and then closes the
 * connection.  It displays the text that it read on
 * standard output.  This program is meant to be used with
 * the server program, DateServer, which sends the current
 * date and time on the computer where the server is running.
 */

public class Client implements Runnable{

    private static String address;
    private static Socket connection; // A socket for communicating with server.
    private int port;
    private ClientTUI view;
    private ServerHandler server;
    private String name;
    private String[] hand;

    public Client(String name, String address ,int port) {
        this.name = name;
        this.port = port;
        this.view = new ClientTUI();
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public ClientTUI getView() {
        return this.view;
    }

    @Override
    public void run() {
        clearConnection();
        createConnection();
        server = new ServerHandler(connection, this);
        server.run();
        new Thread(server).start();
    }

    public void clearConnection() {
        connection = null;
    }

    public synchronized void createConnection() {
        while (connection == null) {
            try {
                view.showMessage("INFO: Attempting to connect to " + address + ":" + port + "...");
                connection = new Socket(InetAddress.getByName(address), port);
                view.showMessage("INFO: Connected at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on " + address + " and port " + port + ".");
                //Do you want to try again? (ask user, to be implemented)
                try {
                    if (!view.getBoolean("QUESTION: Do you want to try to create a new socket? (YES or NO)")) {
                        throw new Exit("INFO: User exited");
                    }
                } catch (InvalidInputException ex) {
                    throw new RuntimeException(ex);
                } catch (Exit ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public synchronized void doPlay() throws InvalidInputException {
        if (this.getName().contains("COMPUTER")) {
            server.sendMessage("MOVE&" + " ");
        }
        else if (view.getBoolean("QUESTION: Do you want to PLAY? (YES or NO) NO means you DRAW")) {
            int index = view.getInt("QUERY: Please input the index of the card to be played");
            server.sendMessage("MOVE&" + hand[index - 1]);
        }
        else {
            server.sendMessage("MOVE&DRAW");
        }
    }

    public synchronized  void doHand(String hand[]) {
        this.hand = hand;
    }

    public static void main (String args[]) {
        System.out.println("QUERY: Please input your name");
        String name = TextIO.getlnString();
        String address = "localhost";
        System.out.println("QUERY: Please input port number to connect to");
        int port = TextIO.getInt();
        Client client = new Client(name, address ,port);
        Thread t = new Thread(client);
        t.start();
    }
}






