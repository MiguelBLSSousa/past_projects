package controller;

import java.util.ArrayList;

public class Pile {
    private ArrayList<Card> cards;

    public Pile() {
        this.cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards(){
        return this.cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public Card getTopCard() {
        return this.cards.get(this.cards.size() - 1);
    }

    public boolean isValid (Card card) {
        boolean valid = false;
        switch(card.getNumber()) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -2, -3:
                if (card.getColor().equals(getTopCard().getColor()) || card.getNumber() == getTopCard().getNumber()) {
                    valid = true;
                }
                break;
            case -4, -5:
                valid = true;
                break;
        }
        return valid;
    }
}
