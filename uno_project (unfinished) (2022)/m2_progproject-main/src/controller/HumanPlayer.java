package controller;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public int determineMove(Deck deck, Pile pile, Player player, String choice) {
        int drawIndex = -10;
        if(choice.equals("DRAW")){
            player.drawCard(deck);
            return drawIndex;
        }
        else{
            String[] cardInfo = choice.split(" ");
            String cardColor = cardInfo[0];
            String cardNumber = cardInfo[1];
            int index = 0;
            for(Card card: player.getHand().getCards()){
                if(card.toString().equals(choice)){
                    return index;
                }
                index++;
            }
        }
        return drawIndex;
    }

    @Override
    public int playablePlusTwo(Deck deck, Pile pile, int current, ArrayList<Player> players) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Card> playableCards = new ArrayList<>();
        int cardIndex = -10;
        for(Card card: players.get(current).getHand().getCards()){
            if(card.getNumber() == -1){
                playableCards.add(card);
            }
        }
        if(playableCards.isEmpty()){
            return cardIndex;
        }
        else{
            System.out.println("HAND:\n" + players.get(current).getHand().toString());
            String playOrDraw = "";
            boolean valid2 = false;
            while (!valid2) {
                System.out.println("PLAY or DRAW (2 and skip turn)");
                playOrDraw = scanner.nextLine();
                if (playOrDraw.equals("PLAY") || playOrDraw.equals("DRAW")) {
                    valid2 = true;
                }
            }
            if (playOrDraw.equals("PLAY")) {
                boolean valid = false;

                while (!valid) {
                    System.out.println("INDEX OF CARD TO PLAY:    , (-5 to abort)");
                    cardIndex = scanner.nextInt();
                    if (pile.isValid(players.get(current).getHand().getCard(cardIndex - 1))) {
                        if(players.get(current).getHand().getCard(cardIndex-1).getNumber() == -1){
                            valid = true;
                        }
                    }
                    else if(cardIndex == -5){
                        return cardIndex;
                    }
                    else {
                        System.out.println("INVALID CARD");
                    }
                }
            }
            else if(playOrDraw.equals("DRAW")){
                return cardIndex;
            }
        }
        return cardIndex;
    }

    @Override
    public int playablePlusFour(Deck deck, Pile pile, int current, ArrayList<Player> players) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Card> playableCards = new ArrayList<>();
        int cardIndex = -10;
        for(Card card: players.get(current).getHand().getCards()){
            if(card.getNumber() == -5){
                playableCards.add(card);
            }
        }
        if(playableCards.isEmpty()){
            return cardIndex;
        }
        else{
            System.out.println("HAND:\n" + players.get(current).getHand().toString());
            String playOrDraw = "";
            boolean valid2 = false;
            while (!valid2) {
                System.out.println("PLAY or DRAW (2 and skip turn)");
                playOrDraw = scanner.nextLine();
                if (playOrDraw.equals("PLAY") || playOrDraw.equals("DRAW")) {
                    valid2 = true;
                }
            }
            if (playOrDraw.equals("PLAY")) {
                boolean valid = false;

                while (!valid) {
                    System.out.println("INDEX OF CARD TO PLAY:    , (-5 to abort)");
                    cardIndex = scanner.nextInt();
                    if (pile.isValid(players.get(current).getHand().getCard(cardIndex - 1))) {
                        if(players.get(current).getHand().getCard(cardIndex-1).getNumber() == -5){
                            valid = true;
                        }
                    }
                    else if(cardIndex == -5){
                        return cardIndex;
                    }
                    else {
                        System.out.println("INVALID CARD");
                    }
                }
            }
            else if(playOrDraw.equals("DRAW")){
                return cardIndex;
            }
        }
        return cardIndex;
    }

    @Override
    public String chooseColor(Pile pile){
        String choice = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("CHOICE OF COLOR:");
        boolean valid = false;
        while(!valid){
            choice = scanner.nextLine();
            if (choice.equals("RED")  || choice.equals("BLUE") || choice.equals("GREEN") || choice.equals("YELLOW")) {
                valid = true;
            }
            else {
                System.out.println("Not A Valid Color, Enter a valid color(RED, BLUE, GREEN, YELLOW");
            }
        }
        return choice;
    }
}