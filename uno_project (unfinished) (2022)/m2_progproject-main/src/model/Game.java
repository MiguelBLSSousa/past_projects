package model;
//test comment
import exceptions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import controller.*;

public class Game implements Runnable {
    //check
    private static int plusFourCounter = 0;
    private static int plusTwoCounter = 0;
    private static ArrayList<Player> players;
    private static int current = 0;
    private static int direction = 1;
    private Deck deck;
    private Pile pile;
    private int playerCount;
    private boolean skip;

    public Game() {
        this.deck = new Deck();
        this.pile = new Pile();
        this.players = new ArrayList<>();
    }

    public static int getPlusFourCounter() {
        return plusFourCounter;
    }

    public static int getPlusTwoCounter() {
        return plusTwoCounter;
    }

    public static void setPlusFourCounter(int plusFourCounter) {
        Game.plusFourCounter = plusFourCounter;
    }

    public static void setPlusTwoCounter(int plusTwoCounter) {
        Game.plusTwoCounter = plusTwoCounter;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void initializeHands(Deck deck, Player player) {
        for (int i = 0; i < 7; i++) {
            player.getHand().addCard(deck.getCards().get(0));
            deck.getCards().remove(0);
        }
    }

    public void initializePile() {
        boolean valid = false;
        while (!valid) {
            if (deck.getCards().get(0).getNumber() == -4 || deck.getCards().get(0).getNumber() == -5) {
                deck.shuffle();
            }
            else {
                valid = true;
            }
        }
        pile.addCard(deck.getCards().get(0));
        deck.getCards().remove(0);
    }

    public void play(String playerName, String playOrDraw) {
        int currentIndex = 0;
        for(Player player: players){
            if(player.getName().equals(playerName)){
                current = currentIndex;
            }
            currentIndex++;
        }
        if (deck.isEmpty()) {
            deck = new Deck();
            deck.shuffle();
        }

        /*
        if(plusTwoCounter > 0){
            int cardPlayedIndex =players.get(current).playablePlusTwo(deck, pile, current, players);
            if(cardPlayedIndex > 0){
                Card played = players.get(current).getHand().getCard(cardPlayedIndex - 1);
                players.get(current).playCard(cardPlayedIndex - 1);
                playedCard(played);
            }
            else{
                for(int i=0; i<plusTwoCounter;i++){
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                }
                plusTwoCounter = 0;
            }
        }
        else if(plusFourCounter>0){
            int cardPlayedIndex =players.get(current).playablePlusFour(deck, pile, current, players);
            if(cardPlayedIndex > 0){
                Card played = players.get(current).getHand().getCard(cardPlayedIndex - 1);
                players.get(current).playCard(cardPlayedIndex - 1);
                playedCard(played);
            }
            else{
                for(int i=0; i<plusFourCounter;i++){
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                    players.get(current).getHand().addCard(deck.getCards().get(0));
                    deck.getCards().remove(0);
                }
                plusFourCounter = 0;
            }
        }

         */
        //else{
            int cardPlayedIndex = players.get(current).determineMove(deck, pile, players.get(current), playOrDraw);
            System.out.println(cardPlayedIndex);

            if (cardPlayedIndex != -10) {
                Card played =  players.get(current).getHand().getCard(cardPlayedIndex);
                if(played.getNumber() == -4 || played.getNumber() == -5){

                }
                else{
                    pile.addCard(played);
                }
                players.get(current).getHand().removeCard(cardPlayedIndex);
                playedCard(played);
        //    }
        }

        if (current == (players.size() - 1) && direction == 1){
            current = 0;
        }
        else if (current == 0 && direction == -1) {
            current = players.size() - 1;
        }
        else {
            current = current + direction;
        }
    }

    public int calculatePoints(){
        int point = 0;
        for(Player player: players){
            if(!player.equals(players.get(current))) {
                for(Card card:player.getHand().getCards()){
                    switch (card.getNumber()){
                        case 0,1,2,3,4,5,6,7,8,9:
                            point += card.getNumber();
                            break;
                        case -1, -2, -3:
                            point += 20;
                        case -4,-5:
                            point += 50;
                    }
                }
            }
        }
        return point;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Pile getPile() {
        return this.pile;
    }

    public Player getPlayer(String name) {
        for (Player player : players) {
                if (player.getName().equals(name)) {
                    return player;
                }
                else if (player.getName().contains("COMPUTER")) {
                    return player;
                }
            }
        return null;
    }

    public void newGame(int playerNum){
        playerCount = playerNum;
        deck.shuffle();
        if (playerNum != 0) {
            for (Player player : players) {
                player.getHand().clearHand();
                initializeHands(deck, player);
            }
        }
        else if (playerNum == 0) {
            Random rand = new Random();
            int random  = rand.nextInt((8 - 2) + 1) + 2;
            for (int i = 0; i < random; i++) {
                Strategy strategy = new NaiveStrategy();
                Player randomComputer = new ComputerPlayer("Computer " + i + 1, strategy);
                players.add(randomComputer);
                initializeHands(deck, randomComputer);
            }
        }
        initializePile();
    }

    public void playedCard(Card card) {

        switch(card.getNumber()) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9:
                break;
            case -1:
                plusTwoCounter++;
                break;
            case -2:
                direction = direction * -1;
                break;
            case -3:
                skip = true;
            case -4:
                pile.addCard(new Card(players.get(current).chooseColor(pile), -100));
                break;
            case -5:
                pile.addCard(new Card(players.get(current).chooseColor(pile), -100));
                plusFourCounter++;
                break;
        }
    }

    public boolean hasRoundWinner(){
        if(players.get(current).getHand().isEmpty()){
            int tempPoints =  players.get(current).getPoints();
            players.get(current).setPoints(tempPoints += calculatePoints());
            return true;
        }
        return false;
    }

    public boolean hasGameWinner(){
        if(players.get(current).getPoints() >= 500){
            int tempPoints =  players.get(current).getPoints();
            players.get(current).setPoints(tempPoints += calculatePoints());
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        deck.shuffle();
        System.out.println("DECK INITIALIZED");

        if (pile.getTopCard() == null) {
            initializePile();
            System.out.println("PILE INITIALIZED");
        }

        if(playerCount < 2 || playerCount > 10){
            try {
                throw new InvalidPlayerAmountException("ERROR: Invalid player amount");
            } catch (InvalidPlayerAmountException e) {
                throw new RuntimeException(e);
            }
        }
    }
}