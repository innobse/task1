package com.innobse.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Class for collect and modified statistic
 *
 *
 * @author Yury Penkov, y.penkov.stc@innopolis.ru
 */

public class StatData2 implements IStatDate {
    private static final int INITIAL_SIZE = 100;
    private static final HashMap<String, Integer> statistic = new HashMap<>(INITIAL_SIZE);
    private static final ReentrantLock rlock = new ReentrantLock();


    /**
     * Update data in model
     *
     * @param word Word in text file
     */

    public void update(String word){    //  Q: ConcurrentHashMap<String, Integer> ?
        try {
            rlock.lock();
            statistic.put(word, (statistic.containsKey(word) ? statistic.get(word) + 1 : 1));
            Main.getCurrentDisplay().printStat();
        } finally {
            rlock.unlock();
        }
    }


    /**
     * Get all map entries
     *
     * @return {@code Set&lt;Map.Entry&lt;String, Integer&gt;&gt;} Set of map entries
     */

    public Set<Map.Entry<String, Integer>> getEntries(){
        Set<Map.Entry<String, Integer>> result = null;
        try {
            rlock.lock();
            result = statistic.entrySet();
        } finally {
            rlock.unlock();
        }
        return result;
    }


    /**
     * Clear data
     *
     */

    public void eraseAll(){
        try {
            rlock.lock();
            statistic.clear();
        } finally {
            rlock.unlock();
        }
    }


    /**
     * Return count of unique words
     *
     */

    public int size(){
        return statistic.size();
    }
}
