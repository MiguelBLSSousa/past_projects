package server;

import exceptions.InvalidInputException;

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable {

    /*
    Direct Commands:
     */
    public static final String WELCOME = "WELCOME";
    public static final String QUEUE = "QUEUE";
    public static final String HAND = "HAND";
    public static final String CHAT = "CHAT";
    public static final String ERROR = "ERROR";

    /*
    Broadcasts:
     */
    public static final String START = "START";
    public static final String UPDATE = "UPDATE";
    public static final String END = "END";
    public static final String WIN = "WIN";
    public static final String TERMINATE = "TERMINATE";
    public static final String FROM = "FROM";

    private static Socket connection; // A socket for communicating with clients.
    private static BufferedReader in; // For reading data from the connection.
    private static BufferedWriter out;
    private Client client;
    public ServerHandler(Socket connection, Client client) {
        try {
            this.connection = connection;
            this.client = client;
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
        } catch (IOException e) {
            shutdown();
        }
    }

    @Override
    public void run() {
        int counter = 0;
        sendMessage("HELLO&" + client.getName());
        boolean exit = false;
        try {
            while (!exit) {
                String msg = in.readLine();
                if (msg != null || msg != "") {
                    //client.getView().showMessage("INCOMING: " + msg);
                    handleCommand(msg);
                }
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        } catch (InvalidInputException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCommand(String msg) throws InvalidInputException {
        boolean split = false;
        for (char check: msg.toCharArray()) {
            if (check == '&') {
                split = true;
                break;
            }
        }

        if (split == true) {
            String msgSplit[] = msg.split("&");
            switch (msgSplit[0]) {
                case WELCOME:
                    boolean playRandom = client.getView().getBoolean("QUESTION: Do you want to play a random game? (YES or NO)");
                    if (!playRandom) {
                        int playerNum = client.getView().getInt("QUESTION: How many players are playing?");
                        sendMessage("PLAY&" + Integer.toString(playerNum));
                    }
                    else {
                        sendMessage("PLAY");
                    }
                    break;
                case QUEUE:
                    String playerQueue[] = msgSplit[1].split(",");
                    client.getView().showMessage("INFO: Player queue");
                    int counter = 1;
                    for (String player: playerQueue) {
                        if (!player.contains("|")) {
                            client.getView().showMessage(counter + ": " + player);
                            counter++;
                        }
                    }
                    if (playerQueue[0].equals(this.client.getName())) {
                        client.doPlay();
                    }
                    else if (playerQueue[0].contains("COMPUTER") && client.getName().contains("COMPUTER")) {
                        client.doPlay();
                    }
                    break;
                case HAND:
                    String hand[] = msgSplit[1].split(",");
                    for (int i = 0; i < hand.length; i++) {
                        if (!hand[i].contains("|")) {
                            client.getView().showMessage((i + 1) + ": " + hand[i]);
                        }
                    }
                    client.doHand(hand);
                    break;
                case START:
                    client.getView().showMessage("INFO: Game is starting!");
                    client.getView().showMessage("TOP CARD: " + msgSplit[2]);
                    break;
                case ERROR:
                    client.getView().showMessage(msgSplit[2]);
                    break;
                case UPDATE:
                    String playerQueue2[] = msgSplit[1].split(",");
                    client.getView().showMessage("INFO: Player queue");
                    int counter2 = 1;
                    for (String player: playerQueue2) {
                        if (!player.contains("|")) {
                            client.getView().showMessage(counter2 + ": " + player);
                            counter2++;
                        }
                    }
                    client.getView().showMessage("TOP CARD: " + msgSplit[2]);
                    if (playerQueue2[0].equals(this.client.getName())) {
                        client.doPlay();
                    }
                    break;
                case END:
                    String players[] = msgSplit[1].split(",");
                    String points[] = msgSplit[2].split(",");
                    client.getView().showMessage(players + "\n" + points);
                    break;
                case WIN:
                    String players2[] = msgSplit[1].split(",");
                    String points2[] = msgSplit[2].split(",");
                    client.getView().showMessage(players2 + "\n" + points2);
                    break;
                case TERMINATE:
                    break;
            }
        }
    }

    private void shutdown() {
        try {
            in.close();
            out.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMessage(String message) {
        if (out != null) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
                //client.getView().showMessage("OUTGOING: " + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
