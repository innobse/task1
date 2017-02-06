package com.innobse.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by bse71 on 06.02.2017.
 */
public class StatData {
    static final HashMap<String, Integer> statistic = new HashMap<>();

    static void update(String word){    //Думаю, это избавит нас от использования ConcurrentHashMap?
        synchronized (statistic){
            statistic.put(word, (statistic.containsKey(word) ? statistic.get(word) + 1 : 1));
            Main.getCurrentDisplay().printStat();
        }
    }

    static Set<Map.Entry<String, Integer>> getEntries(){
        synchronized (statistic){
            return statistic.entrySet();
        }
    }
}
