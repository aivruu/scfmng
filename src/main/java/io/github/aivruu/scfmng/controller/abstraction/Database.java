package io.github.aivruu.scfmng.controller.abstraction;

import java.util.concurrent.CompletableFuture;

/**
 * Represents the base for database-connection and management.
 *
 * @since 1.0.0
 */
public abstract class Database {
  protected final String hostname;
  protected final short port;
  protected final String name;
  protected final String username;
  protected final String password;

  protected Database(
     final String hostname,
     final short port,
     final String name,
     final String username,
     final String password) {
    this.hostname = hostname;
    this.port = port;
    this.name = name;
    this.username = username;
    this.password = password;
  }

  /**
   * Tries to connect to the database using the provided parameters for authentication, and
   * encapsulates and return the result in a {@link CompletableFuture}.
   *
   * @return A {@link CompletableFuture} that will complete with {@code true} if the application
   * successfully connected to the database. Otherwise {@code false}.
   * @since 1.0.0
   */
  public abstract CompletableFuture<Boolean> connect();

  /**
   * Closes the application's connection to the database if it is not closed before.
   *
   * @since 1.0.0
   */
  public abstract void close();
}
