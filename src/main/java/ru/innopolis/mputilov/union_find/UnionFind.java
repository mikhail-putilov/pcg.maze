package ru.innopolis.mputilov.union_find;

/**
 * Created by mputilov on 09.09.16.
 */
public interface UnionFind<T> {
    void union(T a, T b);
    T find(T a);
    boolean isConnected(T a, T b);
    int size();
}
