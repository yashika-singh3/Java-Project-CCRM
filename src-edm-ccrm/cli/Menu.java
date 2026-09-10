package edu.ccrm.cli;

/**
 * Interface for menu handlers in the CLI system.
 * Demonstrates interface design and command pattern.
 */
public interface Menu {
    void display();
    void handleInput();
    String getTitle();
}