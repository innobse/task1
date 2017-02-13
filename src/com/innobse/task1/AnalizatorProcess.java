package com.innobse.task1;


import java.util.ArrayList;

/**
 * Class for analize process
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class AnalizatorProcess extends Thread {
    private String resource;
    private static volatile boolean isCancel = false;


    /**
     * Constructor that takes the path file and the start thread.
     *
     * @param resource the path file
     */

    AnalizatorProcess(String resource){
        this.resource = resource;
        setDaemon(true);            //  нет смысла в потоках, если главный уже вылетел с ошибкой
        start();
    }


    /**
     * Cancel all thread
     *
     */

    public void cancelAll(){
        isCancel = true;
    }


    /**
     * Did thread cancel?
     *
     */

    public boolean isCancelled(){
        return isCancel;
    }


    /**
     * Thread's body
     *
     */

    @Override
    public void run(){
        if (Parser.analize(resource) != 0) this.getThreadGroup().getParent().interrupt();
    }


}
