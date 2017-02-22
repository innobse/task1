package com.innobse.task1;

import static com.innobse.task1.Main.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * Class for analize process
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class AnalizatorProcess extends Thread implements Callable<Integer> {
    private String resource;
    private final int num;
    private final int capacityBuffer;
    private static byte[] file;


    /**
     * Constructor that takes the path file and the start thread.
     *
     * @param resource the path file
     */

    AnalizatorProcess(String resource){
        this(resource, 0, 0, null);
    }

    AnalizatorProcess(String resource, int num, int capacityBuffer, byte[] file){
        this.resource = resource;
        this.num = num;
        this.capacityBuffer = capacityBuffer;
        this.file = file;
    }


    /**
     * Thread's body
     *
     */

    @Override
    public void run(){
        call();
    }


    @Override
    public Integer call() {
        int result = COMPLETE;
        if ((MODE == NIO2) ? Parser.getPartFile(resource, num, capacityBuffer, file) == ERROR : Parser.analize(resource) == ERROR){
            Main.getCurrentDisplay().stop();
            Main.isCancel = true;
            Main.getCurrentDisplay().printErr("One file have incorrect symbols! The program will be closed.");
            result = ERROR;
        }
        if (MODE != DEFAULT) cdl.countDown();
        return result;
    }
}
