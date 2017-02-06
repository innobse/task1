package com.innobse.task1;

/**
 * Main class for task1
 *
 * Вариант 1
 *
 * Необходимо разработать программу, которая получает на вход список ресурсов,
 * содержащих текст, и считает общее количество вхождений (для всех ресурсов)
 * каждого слова. Каждый ресурс должен быть обработан в отдельном потоке, текст
 * не должен содержать инностранных символов, только кириллица, знаки препинания
 * и цифры. Отчет должен строиться в режиме реального времени, знаки препинания
 * и цифры в отчет не входят. Все ошибки должны быть корректно обработаны, все
 * API покрыто модульными тестами
 *
 *
 * @author Yury Penkov, y.penkov@innopolis.ru
 */

public class Main {
    private static Display currentDisplay = new Display();


    /**
     * Program entrypoint
     *
     * @param args filepath or file URL
     */

    public static void main(String[] args) {
        String[] args2 = {"D:\\dev\\Projects\\inno\\testRes\\01.txt", "D:\\dev\\Projects\\inno\\testRes\\02.txt", "D:\\dev\\Projects\\inno\\testRes\\03.txt"};
        for(String t : args2){      //TODO Исправить в релизе
            new AnalizatorProcess(t);
        }
    }


    /**
     * Get print helper
     *
     * @return {@code Display} for print statistics and errors
     */

    public static Display getCurrentDisplay(){
        return currentDisplay;
    }
}
