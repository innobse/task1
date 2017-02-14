package com.innobse.task1;


import java.util.concurrent.atomic.AtomicInteger;

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
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class Main {
    private static Display currentDisplay = new Display();
    static volatile boolean isCancel = false;


    /**
     * Program entrypoint
     *
     * @param args filepath or file URL
     */

    public static void main(String[] args) {
//        Thread[] threads = new Thread[args.length];
//        for(int i = 0; i < args.length; i++){
//            threads[i] = new AnalizatorProcess(args[i]);
//        }
        for (String arg : args) {
            new AnalizatorProcess(arg);
        }
    }


    /**
     * Get print helper
     *
     * @return {@code Display} for print statistics and errors
     */

    static Display getCurrentDisplay(){
        return currentDisplay;
    }
}
