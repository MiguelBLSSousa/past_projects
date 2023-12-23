package server;

import java.util.ArrayList;

import java.util.List;

import controller.*;
import model.Game;


public class Lobby {
    private int number;
    private int size;
    private ArrayList<Player> Players = new ArrayList<>();
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    private Game game;

    /**
     * Creates a new lobby
     * @param number
     * @param size
     */
    public Lobby(int number, int size){
        this.number = number;
        this.size = size;
    }

    /**
     * Adds a player to the lobby
     * @param Player
     */

    public void addPlayer(Player Player) {
        Players.add(Player);
    }

    /**
     * Adds a client to the lobby
     * @param client
     */
    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    /**
     * Retrieve all clients
     * @return array of clients
     */
    public ArrayList<ClientHandler> getClients(){
        return clients;
    }

    /**
     * Retrieve all players
     * @return array of players
     */
    public ArrayList<Player> getPlayers(){
        return Players;
    }

    /**
     * Retrieve all player names
     * @return array of player names
     */
    public ArrayList<String> getPlayerNames(){
        List<Player> x = getPlayers();
        ArrayList<String> names = new ArrayList<String>();
        for (Player c: x) {
            String name = c.getName();
            names.add(name);
        }
        return names;
    }

    /**
     * Retrieve player with a certain name
     * @param name
     * @return controller.Player with the name, null if not found
     */
    public Player getPlayer(String name) {
        for (Player x : Players) {
            if(x.getName().equals(name)) {
                return x;
            }
        }
        return null;
    }

    /**
     * Checks if a player with a certain name is in the lobby
     * @param name
     * @return True if player with name is in lobby, false otherwise
     */
    public boolean hasPlayer(String name) {
        for (Player x: Players) {
            if (x.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the lobby number
     * @return lobby number
     */
    public int getNumber() {
        return number;
    }
    /**
     * Gets the lobby size
     * @return lobby size
     */
    public int getSize() {
        return size;
    }
    /**
     * Sets game that is played in this lobby
     * @param game
     * @ensures this.game = game
     */
    public void setGame(Game game) {
        this.game = game;
    }
    /**
     * Gets the game being played
     * @return game
     */
    public Game getGame() {
        return game;
    }
    /**
     * Check if the lobby is full
     * @return true if size of players array == lobby size, false otherwise
     */
    public boolean isFull() {
        if (getPlayers().size() == getSize()) {
            return true;
        }
        return false;
    }

    /**
     * Check if all players in the lobby are ready
     * @return true if all players ready == true, false otherwise
     */
    public boolean isReady() {
        ArrayList<Player> players = getPlayers();
        for (Player x : players) {
            if (x.isReady() != true) {
                return false;
            }
        }
        return true;
    }
}
