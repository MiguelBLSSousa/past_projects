package controller;

public class Card {
    /*
    DRAW_2 = -1;
    REVERSE = -2;
    SKIP = -3;
    WILD = -4;
    WILD_DRAW_4 = -5;
    */

    private String color;
    private int number;


    public Card(String color, int number){
        this.color = color;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public String toString() {
        switch(number) {
            case 0,1,2,3,4,5,6,7,8,9:
                return this.color.charAt(0) + " " +Integer.toString(number);
            case -1:
                return this.color.charAt(0) + " D2";
            case -2:
                return this.color.charAt(0) + " R";
            case -3:
                return this.color.charAt(0) + " S";
            case -4:
                return this.color.charAt(0) + " W";
            case -5:
                return this.color.charAt(0) + " D4";
        }
        return this.color.charAt(0) + Integer.toString(number);
    }
}
