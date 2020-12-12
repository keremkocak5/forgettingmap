package com.kerem;

import com.kerem.exception.ForgettingMapKeyNotFoundException;
import com.kerem.exception.ForgettingMapKeyNullException;
import com.kerem.exception.ForgettingMapNotInitializedException;
import com.kerem.exception.ForgettingMapValueNullException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ForgettingMapTest {

    private ForgettingMap<String, String> forgettingMap;

    @Test
    public void find_shouldReturnValue_whenKeyFoundInForgettingMap() {
        forgettingMap = new ForgettingMap(3);
        forgettingMap.add("a", "1");
        forgettingMap.add("b", "2");
        forgettingMap.add("c", "3");
        Assertions.assertEquals(forgettingMap.find("a"), "1");
        Assertions.assertEquals(forgettingMap.find("b"), "2");
        Assertions.assertEquals(forgettingMap.find("c"), "3");
        Assertions.assertEquals(forgettingMap.getCapacity(), 3);
        Assertions.assertEquals(forgettingMap.getSize(), 3);
    }

    @Test
    public void find_shouldReturnValue_whenKeyFoundInForgettingMapMultipleTimes() {
        forgettingMap = new ForgettingMap(3);
        forgettingMap.add("a", "1");
        Assertions.assertEquals(forgettingMap.find("a"), "1");
        forgettingMap.add("a", "2");
        Assertions.assertEquals(forgettingMap.find("a"), "2");
        forgettingMap.add("a", "3");
        Assertions.assertEquals(forgettingMap.find("a"), "3");
        Assertions.assertEquals(forgettingMap.getCapacity(), 3);
        Assertions.assertEquals(forgettingMap.getSize(), 1);
    }

    @Test
    public void find_shouldReturnValue_whenKeyFoundInForgettingMapMoreThanCapacity() {
        forgettingMap = new ForgettingMap(3);
        forgettingMap.add("a", "1");
        forgettingMap.add("b", "2");
        Assertions.assertEquals(forgettingMap.find("b"), "2");
        forgettingMap.add("c", "3");
        Assertions.assertEquals(forgettingMap.getSize(), 3);
        forgettingMap.add("b", "4");
        Assertions.assertEquals(forgettingMap.getSize(), 3);
        Assertions.assertEquals(forgettingMap.find("b"), "4");
        Assertions.assertEquals(forgettingMap.find("a"), "1");
        Assertions.assertEquals(forgettingMap.find("c"), "3");
        forgettingMap.add("d", "5");
        Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find("b"));
        Assertions.assertEquals(forgettingMap.find("c"), "3");
        Assertions.assertEquals(forgettingMap.find("d"), "5");
        forgettingMap.add("b", "6");
        Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find("a"));
        Assertions.assertEquals(forgettingMap.find("b"), "6");
        Assertions.assertEquals(forgettingMap.getCapacity(), 3);
        Assertions.assertEquals(forgettingMap.getSize(), 3);
    }

    @Test
    public void find_shouldReturnValue_whenCapacityOne() {
        forgettingMap = new ForgettingMap(1);
        forgettingMap.add("a", "1");
        Assertions.assertEquals(forgettingMap.find("a"), "1");
        Assertions.assertEquals(forgettingMap.getSize(), 1);
        forgettingMap.add("a", "2");
        Assertions.assertEquals(forgettingMap.find("a"), "2");
        Assertions.assertEquals(forgettingMap.getSize(), 1);
        forgettingMap.add("b", "3");
        Assertions.assertEquals(forgettingMap.find("b"), "3");
        Assertions.assertEquals(forgettingMap.getSize(), 1);
        Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find("a"));
    }

    @Test
    public void find_shouldThrowForgettingMapKeyNotFoundException_whenExpiredKeySearched() {
        forgettingMap = new ForgettingMap(3);
        forgettingMap.add("a", "1");
        forgettingMap.add("b", "2");
        forgettingMap.add("c", "3");
        forgettingMap.add("d", "4");
        Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find("a"));
        Assertions.assertEquals(forgettingMap.find("b"), "2");
        Assertions.assertEquals(forgettingMap.find("c"), "3");
        Assertions.assertEquals(forgettingMap.find("d"), "4");
        Assertions.assertEquals(forgettingMap.getSize(), 3);
    }

    @Test
    public void find_shouldReturnValue_whenOldValuesRemoved() {
        ForgettingMap<Integer, Integer> forgettingMap = new ForgettingMap(100);
        for (int i = 0; i < 100; i++) {
            forgettingMap.add(i, i);
            Assertions.assertEquals(forgettingMap.getSize(), i + 1);
        }
        Assertions.assertEquals(forgettingMap.getSize(), 100);
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(forgettingMap.find(i), i);
        }

        for (int i = 100; i < 200; i++) {
            forgettingMap.add(i, i);
            Assertions.assertEquals(forgettingMap.getSize(), 100);
        }
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find(finalI));
        }
        for (int i = 100; i < 200; i++) {
            Assertions.assertEquals(forgettingMap.find(i), i);
        }

        for (int i = 0; i < 10; i++) {
            forgettingMap.add(i, i);
        }
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(forgettingMap.find(i), i);
        }
        for (int i = 110; i < 200; i++) {
            Assertions.assertEquals(forgettingMap.find(i), i);
        }
        for (int i = 100; i < 110; i++) {
            int finalI = i;
            Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find(finalI));
        }
    }

    @Test
    public void find_shouldThrowForgettingMapKeyNotFoundException_whenNonExistentKeySearched() {
        forgettingMap = new ForgettingMap(3);
        Assertions.assertThrows(ForgettingMapKeyNotFoundException.class, () -> forgettingMap.find("x"));
    }

    @Test
    public void find_shouldThrowForgettingMapKeyNullException_whenNullKeySearched() {
        forgettingMap = new ForgettingMap(3);
        Assertions.assertThrows(ForgettingMapKeyNullException.class, () -> forgettingMap.find(null));
    }

    @Test
    public void add_shouldThrowForgettingMapKeyNullException_whenNullKeyAdded() {
        forgettingMap = new ForgettingMap(3);
        Assertions.assertThrows(ForgettingMapKeyNullException.class, () -> forgettingMap.add(null, ""));
    }

    @Test
    public void add_shouldThrowForgettingMapKeyNullException_whenNullValueAdded() {
        forgettingMap = new ForgettingMap(3);
        Assertions.assertThrows(ForgettingMapValueNullException.class, () -> forgettingMap.add("", null));
    }

    @Test
    public void initializeForgettingMapThrowsForgettingMapNotInitializedException_whenCapacitySubZero() {
        Assertions.assertThrows(ForgettingMapNotInitializedException.class, () -> new ForgettingMap(-1));
    }

    @Test
    public void initializeForgettingMapThrowsForgettingMapNotInitializedException_whenCapacityZero() {
        Assertions.assertThrows(ForgettingMapNotInitializedException.class, () -> new ForgettingMap(0));
    }
}