package com.innobse.task1;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
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
 * ReentrantLock (+time)
 * ExecutorService FixedThreadPool (CONCURRENT)
 * java.nio2 (по строкам или по строкам)
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

/*
    По эксперименту: на небольших файлах
        5           в 1.5 - 3 раза
        10          в 1.2 - 2 раза
        25          не существенно быстрее
        200-1000    медленнее на 12-15%

        на маленьких файлах ситуация лучше, но после 30-50 потоков примерно одинаково



 */
public class Main {
    static volatile boolean isCancel = false;
    static int ERROR = -1;
    static int COMPLETE = 0;
    private static final boolean BENCHMARK = false;
    private static int COUNT = 505;
    private static Display currentDisplay = new Display();
    private static long startTime;
    public static volatile IStatDate data;


    /**
     * Program entrypoint
     *
     * @param args filepath or file URL
     */

    public static void main(String[] args) {

        if(BENCHMARK){
            currentDisplay.stop();
            long timeSyn = 0;long timeRee = 0;
            long t1 = 0, t2 = 0;
            for (; COUNT < 1000; COUNT += 50){
                for (int i = 0; i < 50; i++) {
                    try {
                        t1 = startThreads(new StatData(), args[0]);
                        t2 = startThreads(new StatData2(), args[0]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    timeSyn += t1;
                    timeRee += t2;
                }

                System.out.println(COUNT + "\nSynchronized:\t\t" + timeSyn +
                        "\nReentrantLock:\t\t" + timeRee +
                        "\nРазница:\t\t\t" + (timeSyn - timeRee) +
                        "\nПроценты:\t\t\t" + (timeSyn * 100 / timeRee) + "\n\n");
            }
        } else {
//            for (String arg : args) {
//                new AnalizatorProcess(arg).start();
//            }
            ExecutorService es = Executors.newFixedThreadPool(args.length);
            for (String arg : args) {
                es.execute(new AnalizatorProcess(arg));
                //new AnalizatorProcess(arg).start();
            }
            es.shutdown();
        }



    }

    private static long startThreads(IStatDate data, String arg) throws InterruptedException {
        Main.data = data;
        Thread[] ts = new Thread[COUNT];
        startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            ts[i] = new AnalizatorProcess("testRes/04.txt");
            ts[i].start();
        }
        for (Thread t : ts) {
            t.join();
        }
        return System.currentTimeMillis() - startTime;
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
