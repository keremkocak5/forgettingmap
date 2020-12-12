package com.kerem;

import com.kerem.exception.ForgettingMapKeyNotFoundException;
import com.kerem.exception.ForgettingMapKeyNullException;
import com.kerem.exception.ForgettingMapNotInitializedException;
import com.kerem.exception.ForgettingMapValueNullException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class ForgettingMap<K, V> {

    /**
     * A simple map structure, where the goal is to keep the most read values and remove the least read values when the
     * capacity of the ForgettingMap is exceeded.
     *
     * For efficiency, a HashMap and a LinkedList are combined in the ForgettingMap class. A HashMap is efficient  for retrieving
     * a key and can store 'key' and 'content' pairs. However, since a HashMap is not ordered, it is not possible to use it to track
     * the sequence of the elements. That's why a LinkedList is used: it tracks the sequence of the elements.
     *
     * @author Kerem Kocak
     */

    private final int capacity;
    private final Map<K, V> forgettingMap;
    private final LinkedList<K> forgettingLinkedList;

    /**
     * Constructs Forgetting Map. Accepts the capacity of the forgetting map as a parameter.
     * Capacity must be greater than zero.
     *
     * @param capacity max number of elements in the map.
     */
    public ForgettingMap(int capacity) {
        if (capacity < 1) {
            throw new ForgettingMapNotInitializedException();
        }
        this.capacity = capacity;
        this.forgettingMap = new HashMap<>();
        this.forgettingLinkedList = new LinkedList<>();
    }

    /**
     * Adds an element into the Forgetting Map. The key and value are passed into the method.
     * Key is added into the Forgetting Map as the latest element. If the map exceeds the capacity,
     * the least used element is removed.
     * This method is synchronized for the sake of thread-safety.
     *
     * @param key   key of the map. If null, ForgettingMapKeyNullException is thrown
     * @param value value of the map. If null, ForgettingMapValueNullException is thrown
     */
    public void add(K key, V value) {
        if (Objects.isNull(key)) {
            throw new ForgettingMapKeyNullException();
        }
        if (Objects.isNull(value)) {
            throw new ForgettingMapValueNullException();
        }
        moveTheKeyToTopAndRemoveOldestElement(key, value);
    }

    /**
     * Returns the value of the requested key. If key not found, ForgettingMapKeyNotFoundException is thrown.
     * Key is added/updated in the Forgetting Map as the latest element.
     * This method is synchronized for the sake of thread-safety.
     *
     * @param key key of the map. If null, ForgettingMapKeyNullException is thrown
     * @return <V> value of the map.
     */
    public synchronized V find(K key) {
        if (Objects.isNull(key)) {
            throw new ForgettingMapKeyNullException();
        }
        if (!forgettingLinkedList.contains(key)) {
            throw new ForgettingMapKeyNotFoundException();
        }
        return moveTheKeyToTopAndFindValue(key);
    }

    /**
     * Returns the size of the ForgettingMap
     * This method is synchronized for the sake of thread-safety.
     *
     * @return int The size of the ForgettingMap
     */
    public synchronized int getSize() {
        return forgettingLinkedList.size();
    }

    /**
     * Returns the capacity of the ForgettingMap
     *
     * @return int The capacity of the ForgettingMap
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Method to alter the position of the key. Key is removed wherever it is, then added again as the latest element.
     * The value of the key is retrieved from the map.
     *
     * @param key key of the map.
     */
    private V moveTheKeyToTopAndFindValue(K key) {
        forgettingLinkedList.remove(key); // remove the key. cost O(1)
        forgettingLinkedList.add(key); //  add the searched key to the end of the linkedList. cost O(1)
        return forgettingMap.get(key); // retrieve the value of the key from the hashMap. cost O(1)
    }

    /**
     * Method to alter the value of the map, and move the key as the latest element. If the number of elements exceeds the capacity,
     * the oldest one will be removed.
     * This method is synchronized for the sake of thread-safety.
     *
     * @param key   key of the map.
     * @param value value of the map.
     */
    private synchronized void moveTheKeyToTopAndRemoveOldestElement(K key, V value) {
        forgettingMap.put(key, value); // always update the value of the key. cost O(1)
        if (!forgettingLinkedList.isEmpty() && forgettingLinkedList.peekLast().equals(key)) {
            return; // no need to update the linkedList if the key is equal to the linkedList's newest element.
        }
        if (!forgettingLinkedList.isEmpty() && forgettingLinkedList.contains(key)) {
            forgettingLinkedList.remove(key); // if key found in the linkedList, then remove it. cost O(1)
        } else if (forgettingLinkedList.size() >= this.capacity) {
            forgettingLinkedList.remove(); // if the number of items exceeds the capacity, remove the least used one. cost O(1)
        }
        forgettingLinkedList.add(key); // add the key to the linkedList. cost O(1)
    }
}