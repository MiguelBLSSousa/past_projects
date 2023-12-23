package controller;

import java.util.ArrayList;

public interface Strategy {
    String getName();
    public int determineMove(Deck deck, Pile pile, Player player);
}
