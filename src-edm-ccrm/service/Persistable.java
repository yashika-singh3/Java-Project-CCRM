package edu.ccrm.service;

/**
 * Generic interface for persistent operations.
 * Demonstrates generics and interface design.
 */
public interface Persistable<T, ID> {
    T findById(ID id);
    T save(T entity);
    void delete(ID id);
    boolean exists(ID id);
}