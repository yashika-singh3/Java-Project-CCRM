package edu.ccrm.service;

import java.util.List;
import java.util.function.Predicate;

/**
 * Generic interface for search operations.
 * Demonstrates functional interfaces and Stream API usage.
 */
public interface Searchable<T> {
    List<T> findAll();
    List<T> findByPredicate(Predicate<T> predicate);
    long count();
}