package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    /*
        DRAW_2 = -1;
        REVERSE = -2;
        SKIP = -3;
        WILD = -4;
        WILD_DRAW_4 = -5;
    */

    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();

        addNumberedCards("BLUE");
        addNumberedCards("GREEN");
        addNumberedCards("RED");
        addNumberedCards("YELLOW");

        addReverseCards("BLUE");
        addReverseCards("GREEN");
        addReverseCards("RED");
        addReverseCards("YELLOW");

        addDrawCards("BLUE");
        addDrawCards("GREEN");
        addDrawCards("RED");
        addDrawCards("YELLOW");

        addSkipCards("BLUE");
        addSkipCards("GREEN");
        addSkipCards("RED");
        addSkipCards("YELLOW");

        for(int i = 0; i < 4; i++) {
            cards.add(new Card("?",-4));
        }

        for(int i = 0; i < 4; i++) {
            cards.add(new Card("?",-5));
        }
    }

    private void addNumberedCards(String color) {
        int counter = 0;
        for (int i = 0; i < 19; i++) {
            cards.add(new Card(color, counter));
            if (counter == 9) {
                counter = 1;
            }
            else {
                counter++;
            }
        }
    }

    private void addDrawCards(String color) {
        cards.add(new Card(color, -1));
        cards.add(new Card(color, -1));
    }

    private void addReverseCards(String color) {
        cards.add(new Card(color, -2));
        cards.add(new Card(color, -2));
    }

    private void addSkipCards(String color) {
        cards.add(new Card(color, -3));
        cards.add(new Card(color, -3));
    }

    public boolean isEmpty() {
        if (cards.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards, new Random());
    }

    public String toString(){
        String toString = "";
        for (int i = 0; i < cards.size(); i++) {
            toString = toString + cards.get(i) + "\n";
        }
        return toString;
    }
}
