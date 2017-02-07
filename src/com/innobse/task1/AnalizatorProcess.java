package com.innobse.task1;


/**
 * Class for analize process
 *
 *
 * @author Yury Penkov, y.penkov@innopolis.ru
 */

public class AnalizatorProcess extends Thread {
    private String resource;


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
     * Thread's body
     *
     */

    @Override
    public void run(){
        if (Parser.analize(resource) != 0) this.getThreadGroup().getParent().interrupt();
    }


}
