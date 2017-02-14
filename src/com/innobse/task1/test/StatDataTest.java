package com.innobse.task1.test;

import com.innobse.task1.StatData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing StatData
 *
 */
class StatDataTest {
    private static final int COUNT_UPDATE = 10;
    private Class c;
    @BeforeEach
    void setUp() {
        try {
            c = Class.forName("com.innobse.task1.StatData");
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        StatData.eraseAll();
    }

    @Test
    void update() {
        //  Тест уникальности
        Method update = null;
        Method size = null;
        try {
            update = c.getDeclaredMethod("update", String.class);
            update.setAccessible(true);
            size = c.getDeclaredMethod("size");
            size.setAccessible(true);
            for (int i = 0; i < COUNT_UPDATE; i++) {
                String str = "string" + i;
                update.invoke(StatData.class, str);
                assertTrue((Integer) size.invoke(StatData.class) == i + 1);
            }
            for (int i = 0; i < COUNT_UPDATE; i++) {
                String str = "string" + i;
                update.invoke(StatData.class, str);
                assertTrue((Integer) size.invoke(StatData.class) == COUNT_UPDATE);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }

    }

}