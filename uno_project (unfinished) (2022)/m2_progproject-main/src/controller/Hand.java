package controller;

import java.util.ArrayList;
//test comment
public class Hand {
    private ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public Card getCard(int index) {
        return this.cards.get(index);
    }

    public void addCard (Card card) {
        this.cards.add(card);
    }

    public void removeCard (int index) {
        this.cards.remove(index);
    }

    public boolean isEmpty() {
        if (this.cards.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void clearHand(){
        if (!cards.isEmpty()) {
            int index = 0;
            for (Card card : cards) {
                this.removeCard(index);
                index++;
            }
        }
    }

    public String toString() {
        String toString = "";
        for (int i = 0; i < cards.size(); i++) {
            toString = toString + cards.get(i) + ",";
        }
        return toString;
    }
}
