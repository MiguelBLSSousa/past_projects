package test;

import controller.*;
import model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class GameTest {

    Deck deck = new Deck();
    Pile pile = new Pile();
    Game game = new Game();
    Strategy strategy = new NaiveStrategy();
    ArrayList<Player> players = new ArrayList<>();
    ComputerPlayer player1 = new ComputerPlayer("controller.Player 1", strategy);
    ComputerPlayer player2 = new ComputerPlayer("controller.Player 2", strategy);
    ComputerPlayer player3 = new ComputerPlayer("controller.Player 3", strategy);
    ComputerPlayer player4 = new ComputerPlayer("controller.Player 4", strategy);
    @BeforeEach
    void setUp(){
        deck.shuffle();
        game.initializePile();

        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
    }

    @Test
    void initializeHandsTest() {
        game.initializeHands(deck, player1);
        game.initializeHands(deck, player2);
        game.initializeHands(deck, player3);
        game.initializeHands(deck, player4);
        assertFalse(player1.getHand().isEmpty());
        assertEquals(player1.getHand().getCards().size(),7);
        assertFalse(player2.getHand().isEmpty());
        assertEquals(player2.getHand().getCards().size(),7);
        assertFalse(player3.getHand().isEmpty());
        assertEquals(player3.getHand().getCards().size(),7);
        assertFalse(player4.getHand().isEmpty());
        assertEquals(player4.getHand().getCards().size(),7);
    }

    @Test
    void initializePile() {
        game.initializePile();
        assertNotNull(game.getPile().getTopCard());
    }

    @Test
    void playedCard() {
        //Test for played new cards they are on top of the deck
        game.getPile().addCard(new Card("RED", -1));
        assertEquals(game.getPile().getTopCard().getNumber(), -1);
        assertEquals(game.getPile().getTopCard().getColor(), "RED");
        game.getPile().addCard(new Card("GREEN", 2));
        assertEquals(game.getPile().getTopCard().getNumber(), 2);
        assertEquals(game.getPile().getTopCard().getColor(), "GREEN");
        //test plus twos and plus fours
    }
}