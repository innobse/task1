package com.innobse.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


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

    public AnalizatorProcess(String resource){
        this.resource = resource;
        super.start();
    }


    /**
     * Thread's body
     *
     */

    @Override
    public void run(){
        //setDaemon(true);
        analize();
    }


    /**
     * Read file for words and update statistic
     *
     */

    private void analize(){

        try(BufferedReader br = new BufferedReader(new FileReader(resource))) {
            String line;
                while((line = br.readLine()) != null){
                    for(String tempString : line.split("[^а-яА-Я\\-]")){
                        if (!tempString.equals("")) StatData.update(tempString);        //TODO Это надо пофиксить
                    }
                }
        } catch (IOException e) {
            Main.getCurrentDisplay().printErr(e);
        }
        Main.getCurrentDisplay().end(resource);

    }
}
