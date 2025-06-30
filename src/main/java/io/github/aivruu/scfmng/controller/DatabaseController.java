package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.abstraction.Database;
import io.github.aivruu.scfmng.controller.abstraction.MariaDbDatabase;

import java.sql.Connection;

public final class DatabaseController {
  private Database database;

  public Connection connection() {
    return (this.database == null) ? null : ((MariaDbDatabase) this.database).connection();
  }

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

  public void shutdown() {
    if (this.database != null) {
      this.database.close();
    }
  }
}
