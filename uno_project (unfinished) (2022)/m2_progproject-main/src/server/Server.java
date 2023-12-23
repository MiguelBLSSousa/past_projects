package server;

import controller.*;
import model.Game;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server implements Runnable {

    private static ServerSocket connection;  // Listens for incoming connections.
    private List<ClientHandler> clients;
    private ServerTUI view;
    private int nextClientNum;
    private int computerPlayers;
    private Game game;
    private String[] queue;
    /**
     * Constructs a new Server. Initializes the clients list,
     * the view and the nextClientNum.
     */
    public Server() {
        this.clients = new ArrayList<>();
        this.view = new ServerTUI();
        this.nextClientNum = 1;
        this.game = new Game();
        this.computerPlayers = 0;
    }

    public ServerTUI getView() {
        return this.view;
    }

    public List<ClientHandler> getClients() {
        return this.clients;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
        boolean openNewSocket = true;
        while (openNewSocket) {
            try {
                setup();
                while (true) {
                    Socket client = connection.accept();
                    String name = "Client " + String.format("%02d", nextClientNum++);
                    view.showMessage("New client [" + name + "] connected!");

                    ClientHandler handler = new ClientHandler(client, this, name);
                    clients.add(handler);
                    new Thread(handler).start();
                }
            } catch (Exit e1) {
                openNewSocket = false;
            } catch (IOException e) {
                System.out.println("A server IO error occurred: " + e.getMessage());
                if (!view.getBoolean("Do you want to open a new socket?")) {
                    openNewSocket = false;
                }
            }
        }
        view.showMessage("See you later!");
    }

    public void setup() throws Exit {
        // First, initialize the Hotel.
        connection = null;
        while (connection == null) {
            int port = view.getInt("QUERY: Please enter the server port.");
            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 on port " + port + "...");
                connection = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on 127.0.0.1 and port " + port + ".");
                if (!view.getBoolean("Do you want to try again?")) {
                    throw new Exit("User indicated to exit the program.");
                }
            }
        }
    }

    /**
     * Removes a clientHandler from the client list.
     * @requires client != null
     */
    public synchronized void removeClient(ClientHandler client) {
        this.clients.remove(client);
        this.game.getPlayers().remove(client.getName());
    }

    /*
    Command Methods:
     */

    public synchronized void doHello(String playerName, ClientHandler client) {
        if (playerName.equals("COMPUTER")) {
            computerPlayers++;
            client.setName(playerName + " " + computerPlayers);
            Strategy strategy = new NaiveStrategy();
            game.getPlayers().add(new ComputerPlayer(playerName, strategy));
        }
        else {
            client.setName(playerName);
            game.getPlayers().add(new HumanPlayer(playerName));
        }
    }

    public synchronized boolean doPlay(int playerNum) {
        int ready = 0;
        for (ClientHandler client: clients) {
            if (client.getReady()) {
                ready++;
            }
        }
        if (clients.size() == playerNum && ready == playerNum) {
            game.newGame(playerNum);
            game.run();
            doHand();
            return true;
        }
        return false;
    }

    public synchronized void doPlayRandom() {
        game.newGame(0);
        game.run();
        doHand();
    }

    public synchronized void createQueue(String[] queue) {
        this.queue = queue;
    }

    public synchronized  void updateQueue() {
        if (game.getDirection() == 1) {
            String temp = queue[0];
            for (int i = 0; i < queue.length - 1; i++) {
                queue[i] = queue[i + 1];
            }
            queue[queue.length - 1] = temp;
        }
        else if (game.getDirection() == -1) {
            String temp = queue[queue.length];
            for (int i = queue.length - 1; i > 0; i--) {
                queue[i] = queue[i - 1];
            }
            queue[queue.length - 1] = temp;
        }

        if (game.isSkip()) {
            game.setSkip(false);
            updateQueue();
        }
    }

    public synchronized String queueToString() {
        String result = "";
        for (int i = 0; i < queue.length; i++) {
            result = result + queue[i] + ",";
        }
        return result;
    }

    public synchronized void doHand() {
        for (ClientHandler client: clients) {
            client.sendMessage("HAND&" + getGame().getPlayer(client.getName()).getHand().toString() + "|");
        }
    }

    public synchronized void doMove(String playOrDraw, ClientHandler client) {
        if (client.getName().contains("COMPUTER")) {
            game.play(game.getPlayer(client.getName()).getName(), "");
        }
        else {
            game.play(game.getPlayer(client.getName()).getName(), playOrDraw); // playOrDraw either equals "DRAW" or the CARD being played
        }

        if (game.hasRoundWinner()) {
            String broadcast = "";
            for (Player player: game.getPlayers()) {
                broadcast = broadcast + player.getName() + ",";
            }
            broadcast = broadcast + "&";
            for (Player player: game.getPlayers()) {
                broadcast = broadcast + player.getPoints() + ",";
            }
            client.broadcast("END&" + broadcast);
            doNew(clients.size(), client);
        }
        else if (game.hasGameWinner()) {
            String broadcast = "";
            for (Player player: game.getPlayers()) {
                broadcast = broadcast + player.getName() + ",";
            }
            broadcast = broadcast + "&";
            for (Player player: game.getPlayers()) {
                broadcast = broadcast + player.getPoints() + ",";
            }
            client.broadcast("WIN&" + broadcast);
        }
        else {
            doHand();
            updateQueue();
            client.broadcast("UPDATE&" + queueToString() + "&" + game.getPile().getTopCard().toString());
        }
    }

    public void doNew(int playerNum, ClientHandler client) {
        game.newGame(playerNum);
        game.run();
        doHand();
        client.broadcast("START&" + queueToString() + "&" + getGame().getPile().getTopCard().toString() + "|");
        client.broadcast("QUEUE&" + queueToString() + "|");
    }

    public synchronized  void doTake(ClientHandler client) {

    }

    public static void main (String args[]) {
        Server server = new Server();
        Thread t = new Thread(server);
        t.start();
    }
}
