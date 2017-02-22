package com.innobse.task1;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;
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
    //  НЕ ТРОГАТЬ!
    static volatile boolean isCancel = false;
    static int ERROR = -1;
    static int COMPLETE = 0;
    public static final boolean REENTRANTLOCK = true;
    public static final int DEFAULT = 0;
    public static final int BENCHMARK = 1;
    public static final int EXECUTORSERVICE = 2;
    public static final int NIO2 = 3;

    //  режимы (допники) модифицируемо
    public static final int MODE = NIO2;
    public static final int NIO2_COUNT_THREADS = 4;
    public static final boolean MODEDATA = REENTRANTLOCK;
    private static int COUNT = 505;

    //  переменные
    private static Display currentDisplay = new Display();
    private static long startTime;
    public static volatile IStatDate data;
    public static CountDownLatch cdl;
    public static CountDownLatch cdlN;


    /**
     * Program entrypoint
     *
     * @param args filepath or file URL
     */

    public static void main(String[] args) {

        if(MODEDATA) data = new StatData2();
        else new StatData();

        switch(MODE){
            case BENCHMARK:         mainBench(args); break;
            case EXECUTORSERVICE:   mainExecSrv(args);break;
            case NIO2:              mainNIO2(args); break;
            case DEFAULT:
            default:                mainDefault(args); break;
        }
    }

    public static void mainDefault(String[] args) {
        for (String arg : args) {
            new AnalizatorProcess(arg).start();
        }
    }

    public static void mainBench(String[] args) {
        currentDisplay.stop();
        long timeSyn = 0;long timeRee = 0;
        long t1 = 0, t2 = 0;
        for (; COUNT < 1000; COUNT += 50) {
            for (int i = 0; i < 50; i++) {
                try {
                    t1 = startThreads(new StatData(), "testRes/05.txt");
                    t2 = startThreads(new StatData2(), "testRes/05.txt");
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
    }

    public static void mainExecSrv(String[] args) {
        cdl = new CountDownLatch(args.length);
        ExecutorService es = Executors.newFixedThreadPool(args.length);
        for (String arg : args) {
            es.execute(new AnalizatorProcess(arg));
        }
        try{
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("END SUB");
        es.shutdown();
    }

    public static void mainNIO2(String[] args) {
        String target = "testRes/06.txt";//args[0];
        File file = new File(target);
        long size = 0;
        if (file.exists()) size = file.length();
        long offset = size / NIO2_COUNT_THREADS + ((size % NIO2_COUNT_THREADS == 0) ? 0 : 1);
        final int capacity = (int) offset;

        cdl = new CountDownLatch(NIO2_COUNT_THREADS);
        cdlN = new CountDownLatch(NIO2_COUNT_THREADS);
        ExecutorService es = Executors.newFixedThreadPool(NIO2_COUNT_THREADS);
        byte[] tmp = new byte[(int) (NIO2_COUNT_THREADS * capacity)];
        ArrayList<Callable<Integer>> tasks = new ArrayList<>(NIO2_COUNT_THREADS);
        for (int i = 0; i < NIO2_COUNT_THREADS; i++) {
            tasks.add(new AnalizatorProcess(target, i, capacity, tmp));
        }
        try{
            es.invokeAll(tasks);
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentDisplay.printStat();
        System.out.println("КОНЕЦ ИСПОЛНЕНИЯ ПРОГРАММЫ");
        es.shutdown();
    }

    private static long startThreads(IStatDate data, String arg) throws InterruptedException {
        Main.data = data;
        Thread[] ts = new Thread[COUNT];
        startTime = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            ts[i] = new AnalizatorProcess(arg);
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
