package com.cleanroommc.modularui.utils;

import com.google.common.collect.ForwardingList;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface ObjectList<V> extends List<V>, Comparable<List<? extends V>> {

    static <V> ObjectArrayList<V> create() {
        return new ObjectArrayList<>();
    }

    static <V> ObjectArrayList<V> create(int size) {
        return new ObjectArrayList<>(size);
    }

    static <V> ObjectArrayList<V> of(Collection<? extends V> c) {
        return new ObjectArrayList<>(c);
    }

    static <V> ObjectArrayList<V> of(V[] a) {
        return new ObjectArrayList<>(a);
    }

    static <V> ObjectArrayList<V> of(V[] a, int offset, int length) {
        return new ObjectArrayList<>(a, offset, length);
    }

    static <V> ObjectArrayList<V> of(Iterator<? extends V> i) {
        return new ObjectArrayList<>(i);
    }

    void addFirst(V v);

    void addLast(V v);

    V getFirst();

    V getLast();

    V removeFirst();

    V removeLast();

    @Nullable
    V peekFirst();

    @Nullable
    V pollFirst();

    @Nullable
    V peekLast();

    @Nullable
    V pollLast();

    void trim();

    V[] elements();

    class ObjectArrayList<V> extends ForwardingList<V> implements ObjectList<V> {

        final List<V> delegate;

        public ObjectArrayList(int capacity) {
            this.delegate = new ArrayList<>(capacity);
        }

        public ObjectArrayList() {
            this.delegate = new ArrayList<>();
        }

        public ObjectArrayList(Collection<? extends V> c) {
            this.delegate = new ArrayList<>(c);
        }

        public ObjectArrayList(V[] a) {
            this.delegate = new ArrayList<>(Arrays.asList(a));
        }

        public ObjectArrayList(V[] a, int offset, int length) {
            this(Arrays.copyOfRange(a, offset, offset + length));
        }

        public ObjectArrayList(Iterator<? extends V> i) {
            this.delegate = Lists.newArrayList(i);
        }

        @Override
        protected List<V> delegate() {
            return this.delegate;
        }

        @Override
        public void addFirst(V v) {
            add(0, v);
        }

        @Override
        public void addLast(V v) {
            add(v);
        }

        @Override
        public V getFirst() {
            return get(0);
        }

        @Override
        public V getLast() {
            return get(size() - 1);
        }

        @Override
        public V removeFirst() {
            return remove(0);
        }

        @Override
        public V removeLast() {
            return remove(size() - 1);
        }

        @Override
        public V peekFirst() {
            return isEmpty() ? null : getFirst();
        }

        @Override
        public V pollFirst() {
            return isEmpty() ? null : removeFirst();
        }

        @Override
        public V peekLast() {
            return isEmpty() ? null : getLast();
        }

        @Override
        public V pollLast() {
            return isEmpty() ? null : removeLast();
        }

        @Override
        public int compareTo(@NotNull List<? extends V> o) {
            // I don't think this will be used
            throw new UnsupportedOperationException();
        }

        @Override
        public void trim() {
            // Kept just for preserving consistency with 1.12.2 code. It looks to be not that important anyway.
        }

        @Override
        public V[] elements() {
            return (V[]) delegate.toArray();
        }
    }
}
