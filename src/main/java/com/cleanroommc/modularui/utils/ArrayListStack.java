package com.cleanroommc.modularui.utils;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Extension of ArrayList, inspired by it.unimi.dsi.fastutil.Stack
 */
public class ArrayListStack<E> extends ArrayList<E> {

    public void push(E o) {
        add(o);
    }

    public E pop() {
        if (isEmpty()) throw new NoSuchElementException();
        return remove(size() - 1);
    }

    public E top() {
        if (isEmpty()) throw new NoSuchElementException();
        return get(size() - 1);
    }
}
