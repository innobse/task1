package com.innobse.task1;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Print helper class. Print information to some output stream
 *
 *
 * @author Yury Penkov, y.penkov@innopolis.ru
 */

public class Display {
    private volatile boolean canPrint = true;


    /**
     * Print statistics to console
     *
     */

    void printStat(){
        if (!canPrint) return;
        System.out.println("===================================================================");
        for(ConcurrentHashMap.Entry entry : StatData.getEntries()){
            System.out.println(entry.getKey() + "        " + entry.getValue());
        }
        System.out.println("===================================================================\n\n");
    }

    /**
     * Print error message to console
     *
     * @param err Detected exception
     */

    void printErr(Exception err){
        System.out.println("Error detected! " + err.getMessage() + " The result may be incorrect!");
    }

    /**
     * Print error message to console
     *
     * @param err Exception message
     */

    void printErr(String err){
        System.out.println("Error detected! " + err + " The result may be incorrect!");
    }

    /**
     * Print special message when thread stopped
     *
     * @param res path or URL for txt file
     */

    void end(String res){
        System.out.println("Все слова в файле: " + res + " посчитаны!");
    }


    /**
     * Stop all print
     *
     */

    void stop(){
        canPrint = false;
    }


}
