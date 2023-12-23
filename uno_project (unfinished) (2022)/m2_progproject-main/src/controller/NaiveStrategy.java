package controller;

import java.util.ArrayList;

public class NaiveStrategy implements Strategy{

    @Override
    public String getName() {
        return "Naive controller.Strategy";
    }

    @Override
    public int determineMove(Deck deck, Pile pile, Player player) {
        int countIndex = 0;
        for(Card card:player.getHand().getCards()){
            if(pile.isValid(card)){
                return (countIndex+1);
            }
            countIndex++;
        }
        player.drawCard(deck);
        return -10;
    }
}