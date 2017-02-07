package com.innobse.task1.test;

import static com.innobse.task1.Parser.analize;
import com.innobse.task1.StatData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ParserTest {
    private static String[] paths = {"testRes/01.txt", "testRes/02.txt", "testRes/03.txt"};
    HashMap<String, Integer> expect = new HashMap<>();      //  начального значения как раз хватит


    @Before
    public void setUp() throws Exception {
        expect.put("один", 1);
        expect.put("два", 2);
        expect.put("три", 3);
        expect.put("четыре", 4);
        expect.put("пять", 5);
        expect.put("шесть", 6);
        expect.put("всего-то", 1);
        expect.put("вот-вот", 1);
        expect.put("превед", 1);
        expect.put("медвед", 1);
    }

    @After
    public void tearDown() throws Exception {
        expect.clear();
    }

    @Test
    public void testAnalize() throws Exception {


        //  Проверка на тестовом наборе файлов
        for(String str : paths){
            analize(str);
        }
        Set<Map.Entry<String, Integer>> result = StatData.getEntries();
        assertTrue(result.size() == expect.size());
        for(Map.Entry<String, Integer> entry : result){
            assertEquals(expect.get(entry.getKey()), entry.getValue());
        }

        //  Проверка на возврат при нормальных данных
        assertTrue(analize("testRes/03.txt") == 0);
        assertTrue(analize("testRes/01.txt") == 0);

        //  Проверка на возврат при некорректных символах в файлах
        assertTrue(analize("http://joomla.ru/README.txt") == -1);
        assertTrue(analize("testRes/04.txt") == -1);

        //  Проверка на возврат и нормальное завершение при несуществующих файлах
        assertTrue(analize("http://joomla.ru/README2.txt") == -1);
        assertTrue(analize("testRes/not02.txt") == -1);
    }
}