package com.innobse.task1;

public class Main {
    private static Display currentDisplay = new Display();

    public static void main(String[] args) {
        String[] args2 = {"D:\\dev\\Projects\\inno\\testRes\\01.txt", "D:\\dev\\Projects\\inno\\testRes\\02.txt", "D:\\dev\\Projects\\inno\\testRes\\03.txt"};
        for(String t : args2){      //TODO Исправить в релизе
            new AnalizatorProcess(t);
        }
    }

    public static Display getCurrentDisplay(){
        return currentDisplay;
    }
}
