package com.innobse.task1;

import java.util.Map;
import java.util.Set;

/**
 * Created by bse71 on 16.02.2017.
 */
public interface IStatDate {

    /**
     * Update data in model
     *
     * @param word Word in text file
     */

    void update(String word);

    /**
     * Get all map entries
     *
     * @return {@code Set&lt;Map.Entry&lt;String, Integer&gt;&gt;} Set of map entries
     */

    Set<Map.Entry<String, Integer>> getEntries();


    /**
     * Clear data
     */

    void eraseAll();


    /**
     * Return count of unique words
     */

    int size();

}
