package com.innobse.task1.test;

import com.innobse.task1.Parser;
import com.innobse.task1.StatData;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by bse71 on 13.02.2017.
 */
class ParserTest {
    private static String[] paths = {"testRes/01.txt", "testRes/02.txt", "testRes/03.txt"};
    private HashMap<String, Integer> expect = new HashMap<>();      //  начального значения как раз хватит

    @BeforeEach
    public void setUp() throws Exception {

    }

    private void initData(){
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

    @AfterEach
    public void tearDown() throws Exception {
        StatData.eraseAll();
    }

    @Test
    public void testAnalize() throws Exception {
        //  Проверка на тестовом наборе файлов
        initData();
        for(String str : paths){
            Parser.analize(str);
        }
        Set<Map.Entry<String, Integer>> result = StatData.getEntries();
        assertTrue(expect.size() == result.size());
        for(Map.Entry<String, Integer> entry : result){
            assertEquals(expect.get(entry.getKey()), entry.getValue(), "Несовпадение по ключу \'" + entry.getKey() + "\'");
        }
    }

    @Test
    public void testAnalizeReturnValues() throws Exception {

        //  Проверка на возврат при некорректных символах в файлах
        assertTrue(Parser.analize("http://joomla.ru/README.txt") == -1);
        assertTrue(StatData.getEntries().size() == 0);
        assertTrue(Parser.analize("testRes/04.txt") == -1);
        assertTrue(StatData.getEntries().size() == 0);

        //  Проверка на возврат и нормальное завершение при несуществующих файлах
        assertTrue(Parser.analize("http://joomla.ru/README2.txt") == -1);
        assertTrue(StatData.getEntries().size() == 0);
        assertTrue(Parser.analize("testRes/not02.txt") == -1);
        assertTrue(StatData.getEntries().size() == 0);

        //  Проверка на возврат при нормальных данных
        int semiResult = 0;
        assertTrue(Parser.analize("testRes/03.txt") == 0);
        assertTrue((semiResult = StatData.getEntries().size()) > 0);
        assertTrue(Parser.analize("testRes/01.txt") == 0);
        assertTrue(StatData.getEntries().size() > semiResult);
    }

}