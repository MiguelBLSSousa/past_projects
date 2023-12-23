package controller;

import java.util.ArrayList;

public class ComputerPlayer extends Player{

    Strategy strategy;

    public ComputerPlayer(String name, Strategy strategy) {
        super(name);
        this.strategy = strategy;
    }

    @Override
    public int determineMove(Deck deck, Pile pile, Player player, String choice) {
        return strategy.determineMove(deck, pile, player);
    }

    @Override
    public int playablePlusTwo(Deck deck, Pile pile, int current, ArrayList<Player> players) {
        int cardIndex = 0;
        for(Card card: players.get(current).getHand().getCards()){
            if(card.getNumber() == -1){
                return cardIndex;
            }
            cardIndex++;
        }
        return -10;
    }

    @Override
    public int playablePlusFour(Deck deck, Pile pile, int current, ArrayList<Player> players) {
        int cardIndex = 0;
        for(Card card: players.get(current).getHand().getCards()){
            if(card.getNumber() == -5){
                return cardIndex;
            }
            cardIndex++;
        }
        return -10;
    }

    @Override
    public String chooseColor(Pile pile) {
        String color = pile.getTopCard().getColor();
        switch (color){
            case "GREEN":
                color = "BLUE";
                break;
            case "YELLOW":
                color = "GREEN";
                break;
            case "RED":
                color = "YELLOW";
                break;
            case "BLUE":
                color = "RED";
                break;
        }
        return color;
    }
}