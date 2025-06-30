package io.github.aivruu.scfmng.controller.abstraction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * {@link Database} implementation for MariaDB database support.
 *
 * @since 1.0.0
 */
public final class MariaDbDatabase extends Database {
  private static final String CONNECTION_URL = "jdbc:mysql://%s:%d/%s";
  private Connection connection;

  /**
   * Creates a new {@link MariaDbDatabase} instance with the provided parameters.
   *
   * @param hostname the database's host.
   * @param port the host's port.
   * @param name the database's name.
   * @param username the database's username.
   * @param password the database's password.
   * @since 1.0.0
   */
  public MariaDbDatabase(
     final String hostname,
     final short port,
     final String name,
     final String username,
     final String password) {
    super(hostname, port, name, username, password);
  }

  /**
   * Returns the {@link Connection} instance for the database.
   *
   * @return The {@link Connection} or {@code null} if the {@link #connection}'s {@code null} or
   * it's closed.
   * @since 1.0.0
   */
  public Connection connection() {
    return this.connection;
  }

  /**
   * {@inheritDoc}
   * <p>
   * For this implementation, if the connection is already established, the method will just return
   * {@code false}.
   *
   * @return A {@link CompletableFuture} that will be completed with {@code true} if the
   * connection was established successfully. Otherwise {@code false}.
   * @since 1.0.0
   */
  @Override
  public CompletableFuture<Boolean> connect() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        if (this.connection != null && !this.connection.isClosed()) {
          return false;
        }
        this.connection = DriverManager.getConnection(
           CONNECTION_URL.formatted(super.hostname, super.port, super.name), this.username,
           this.password);
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  @Override
  public void close() {
    if (this.connection == null) {
      return;
    }
    try {
      if (!this.connection.isClosed()) {
        this.connection.close();
      }
    } catch (final SQLException exception) {
      exception.printStackTrace();
    }
  }
}
