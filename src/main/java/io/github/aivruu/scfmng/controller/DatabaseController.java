package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.database.Database;
import io.github.aivruu.scfmng.controller.database.MariaDbDatabase;

import java.sql.Connection;

/**
 * Controller used for database-connection and management.
 *
 * @since 1.0.0
 */
public final class DatabaseController {
  private Database database;

  /**
   * Returns the {@link Connection} for the current database.
   * <p>
   * It should be now that the connection-provided could be closed, so it is required to check by
   * connection-availability before using it.
   *
   * @return The {@link Connection} (might be closed), or {@code null} if the database has not been
   * initialized yet.
   * @since 1.0.0
   */
  public Connection connection() {
    return (this.database == null) ? null : ((MariaDbDatabase) this.database).connection();
  }

  /**
   * Tries to connect with the database using the provided parameters.
   * <p>
   * If the application is already connected to the database, the method will stop and return
   * {@code false}.
   *
   * @param hostname the database's host/server.
   * @param port the host's port.
   * @param database the database's name.
   * @param user the database's user.
   * @param password the database's user's password.
   * @return {@code true} if the application connected to the database, {@code false} otherwise.
   * @see Database#connect()
   * @since 1.0.0
   */
  public boolean connect(
     final String hostname,
     final short port,
     final String database,
     final String user,
     final String password) {
    if (this.database != null) {
      return false;
    }
    return (this.database = new MariaDbDatabase(hostname, port, database, user, password)).connect()
       .exceptionally(exception -> {
         exception.printStackTrace();
         return false;
       })
       .join();
  }

  /**
   * Closes the application's connection with the database if it is not closed yet.
   *
   * @since 1.0.0.
   */
  public void shutdown() {
    if (this.database != null) {
      this.database.close();
    }
  }
}
