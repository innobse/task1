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


    /**
     * Constructor that takes the path file and the start thread.
     *
     * @param resource the path file
     */

    AnalizatorProcess(String resource){
        this.resource = resource;
        start();
    }


    /**
     * Thread's body
     *
     */

    @Override
    public void run(){
        if (Parser.analize(resource) != 0){
            Main.isCancel = true;
            Main.getCurrentDisplay().stop();
            Main.getCurrentDisplay().printErr("One file have incorrect symbols! The program will be closed.");
        }
    }


}
