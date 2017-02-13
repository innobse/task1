package com.innobse.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Class for collect and modified statistic
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class StatData {
    private static final int INITIAL_SIZE = 100;
    private static final HashMap<String, Integer> statistic = new HashMap<>(INITIAL_SIZE);


    /**
     * Update data in model
     *
     * @param word Word in text file
     */

    static void update(String word){    //  Q: Как оптимизировать? ConcurrentHashMap<String, AtomicInteger> ?
        synchronized (statistic){
            statistic.put(word, (statistic.containsKey(word) ? statistic.get(word) + 1 : 1));
            Main.getCurrentDisplay().printStat();
        }
    }


    /**
     * Get all map entries
     *
     * @return {@code Set&lt;Map.Entry&lt;String, Integer&gt;&gt;} Set of map entries
     */

    public static Set<Map.Entry<String, Integer>> getEntries(){
        synchronized (statistic){
            return statistic.entrySet();
        }
    }


    /**
     * Clear data
     *
     */

    public static void eraseAll(){
        synchronized (statistic){
            statistic.clear();
        }
    }
}
