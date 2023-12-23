package controller;

import java.util.ArrayList;
//test comment
public abstract class Player {
    private String name;
    private Hand hand;

    private int points;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.points = 0;
    }

    public String getName() {
        return this.name;
    }

    public Hand getHand() {
        return this.hand;
    }

    public void drawCard(Deck deck) {
        this.hand.addCard(deck.getCards().get(0));
        deck.getCards().remove(0);
    }

    public void playCard(int index) {
        this.hand.removeCard(index);
    }

    public abstract int determineMove(Deck deck, Pile pile, Player player, String choice);

    public abstract int playablePlusTwo(Deck deck, Pile pile, int current, ArrayList<Player> players);

    public abstract int playablePlusFour(Deck deck, Pile pile, int current, ArrayList<Player> players);

    public abstract String chooseColor(Pile pile);

    public boolean isReady() {
        return true;
    }

    public int getPoints(){
        return this.points;
    }

    public void setPoints(int points){
        this.points = points;
    }
}