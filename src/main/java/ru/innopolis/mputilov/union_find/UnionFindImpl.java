package ru.innopolis.mputilov.union_find;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mputilov on 09.09.16.
 */
public class UnionFindImpl<T> implements UnionFind<T> {
    private int size = 0;
    private Map<T, T> connectionMap;

    public UnionFindImpl() {
        connectionMap = new HashMap<>();
    }

    public UnionFindImpl(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size cannot be negative");
        }
        this.size = size;
        connectionMap = new HashMap<>(size);
    }

    @Override
    public void union(T a, T b) {
        connectionMap.put(a, b);
    }

    @Override
    public T find(T a) {

        return null;
    }

    @Override
    public boolean isConnected(T a, T b) {
        return false;
    }

    @Override
    public int size() {
        return size;
    }
}
