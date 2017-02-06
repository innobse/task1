package com.innobse.task1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by @author(bse71) on 06.02.2017.
 */
public class AnalizatorProcess extends Thread {
    private String resource;

    public AnalizatorProcess(String resource){
        this.resource = resource;
        super.start();
    }

    @Override
    public void run(){
        //setDaemon(true);
        analize();
    }

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
