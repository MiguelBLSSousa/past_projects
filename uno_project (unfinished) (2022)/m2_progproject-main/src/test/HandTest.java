package test;

import controller.Card;
import controller.Hand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    Hand hand = new Hand();

    @BeforeEach
    void setUp() {
        hand.addCard(new Card("RED", 2));
        hand.addCard(new Card("GREEN", 0));
        hand.addCard(new Card("YELLOW", 7));
    }

    @Test
    void getCards() {
        //assertInstanceOf(ArrayList<Card>, hand.getCards());
    }

    @Test
    void getCard() {
        assertNotNull(hand.getCard(0));
        assertTrue(hand.getCard(0) instanceof Card);
        assertEquals(hand.getCard(0).getNumber(), 2);
        assertEquals(hand.getCard(0).getColor(), "RED");
        assertNotNull(hand.getCard(1));
        assertTrue(hand.getCard(1) instanceof Card);
        assertEquals(hand.getCard(1).getNumber(), 2);
        assertEquals(hand.getCard(1).getColor(), "RED");
    }

    @Test
    void addCard() {
    }

    @Test
    void removeCard() {
    }

    @Test
    void isEmpty() {
    }

    @Test
    void clearHand() {
    }

    @Test
    void testToString() {
    }
}