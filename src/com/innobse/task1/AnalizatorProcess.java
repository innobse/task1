package com.innobse.task1;

import static com.innobse.task1.Main.ERROR;
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
    }


    /**
     * Thread's body
     *
     */

    @Override
    public void run(){
        if (Parser.analize(resource) == ERROR){
            Main.getCurrentDisplay().stop();
            Main.isCancel = true;
            Main.getCurrentDisplay().printErr("One file have incorrect symbols! The program will be closed.");
        }
    }


}
