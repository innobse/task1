package com.innobse.task1;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bse71 on 06.02.2017.
 */
public class Display {

    void printStat(){
        System.out.println("===================================================================");
        for(ConcurrentHashMap.Entry entry : StatData.getEntries()){
            System.out.println(entry.getKey() + "        " + entry.getValue());
        }
        System.out.println("===================================================================\n\n");
    }

    void printErr(Exception err){
        System.out.println("Error detected!" + err.getMessage() + "The result may be incorrect!");
    }

    void end(String res){
        System.out.println("Все слова в фвйле: " + res + " посчитаны!");
    }


}
