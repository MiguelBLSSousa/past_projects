package test;

import controller.Card;
import controller.Pile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    Pile pile = new Pile();

    @Test
    void getCards(){
        assertTrue(pile.getCards().isEmpty());
    }

    @Test
    void addCard() {
        Card card = new Card("RED", 0);
        pile.addCard(card);
        assertFalse(pile.getCards().isEmpty());
        assertTrue(pile.getCards().get(0).getColor().equals("RED"));
    }

    @Test
    void getTopCard() {
        Card card = new Card("RED", 0);
        Card topCard = new Card("YELLOW", 2);
        pile.addCard(card);
        pile.addCard(topCard);
        assertFalse(pile.getTopCard().equals(card));
        assertTrue(pile.getTopCard().equals(topCard));
    }

    @Test
    void isValid() {
        pile.addCard(new Card("RED", 0));
        assertTrue(pile.isValid(new Card("RED", 7)));
        assertFalse(pile.isValid(new Card("GREEN", 3)));
        assertFalse(pile.isValid(new Card("RED", 20)));
    }
}