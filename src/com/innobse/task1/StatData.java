package com.innobse.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Class for collect and modified statistic
 *
 *
 * @author Yury Penkov, y.penkov@innopolis.ru
 */

public class StatData {
    static final HashMap<String, Integer> statistic = new HashMap<>();


    /**
     * Update data in model
     *
     * @param word Word in text file
     */

    static void update(String word){    //Думаю, это избавит нас от использования ConcurrentHashMap?
        synchronized (statistic){
            statistic.put(word, (statistic.containsKey(word) ? statistic.get(word) + 1 : 1));
            Main.getCurrentDisplay().printStat();
        }
    }


    /**
     * Get all map entries
     *
     * @return {@code Set<Map.Entry<String, Integer>>} Set of map entries
     */

    static Set<Map.Entry<String, Integer>> getEntries(){
        synchronized (statistic){
            return statistic.entrySet();
        }
    }
}
