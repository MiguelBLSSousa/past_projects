package server;

import controller.HumanPlayer;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    /*
    Direct Commands:
     */
    public static final String HELLO = "HELLO";
    public static final String PLAY = "PLAY";
    public static final String MOVE = "MOVE";
    public static final String TAKE = "TAKE"; //for progressive uno only
    public static final String CHANGE = "CHANGE";

    private Socket connection; // A socket for communicating with server.
    private BufferedReader in; // For reading data from the connection.
    private BufferedWriter out;
    private boolean ready;
    private Server server;
    private String name;

    public ClientHandler(Socket connection, Server server, String name) {
        try {
            this.name = name;
            this.connection = connection;
            this.server = server;
            this.ready = false;
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
        } catch (IOException e) {
            shutdown();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void isReady() {
        this.ready = true;
    }

    public boolean getReady() {
        return this.ready;
    }

    @Override
    public void run() {
        boolean exit = false;
        try {
            while (!exit) {
                String msg = in.readLine();
                if (msg != null || msg != "") {
                    server.getView().showMessage("INCOMING: " + msg);
                    handleCommand(msg);
                }
            }
            shutdown();
        } catch (IOException e) {
            shutdown();
        }
    }

    private void handleCommand(String msg) {
        //check if msg is null
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
                case HELLO:
                    server.doHello(msgSplit[1], this);
                    sendMessage("WELCOME&" + msgSplit[1] + "|");
                    break;
                case PLAY:
                    int playerNum = Integer.parseInt(msgSplit[1]);
                    isReady();
                    if (server.doPlay(playerNum)) {
                        String outgoing[] = new String[server.getClients().size()];
                        int i = 0;
                        for (ClientHandler client: server.getClients()) {
                            outgoing[i] = client.getName();
                            i++;
                        }
                        server.createQueue(outgoing);
                        broadcast("START&" + server.queueToString() + "&" + server.getGame().getPile().getTopCard().toString() + "|");
                        broadcast("QUEUE&" + server.queueToString() + "|");
                    }
                    break;
                case MOVE:
                    server.doMove(msgSplit[1], this);
                    break;
            }
        }

        else {
            switch (msg) {
                case PLAY:
                    server.doPlayRandom();
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
        server.removeClient(this);
    }

    public synchronized void sendMessage(String message) {
        if (out != null) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
                server.getView().showMessage("OUTGOING: " + message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void broadcast(String message) {
        for (int i = 0; i < server.getClients().size(); i++) {
            ClientHandler client = server.getClients().get(i);
            client.sendMessage(message);
        }
    }
}
