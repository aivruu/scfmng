package io.github.aivruu.scfmng.controller.abstraction;

public final class StatementConstants {
  public static final String CLIENT_TABLE_CREATE = """
     CREATE TABLE IF NOT EXISTS clients(
       id VARCHAR(80) PRIMARY KEY NOT NULL,
       name VARCHAR(20),
       last_name_one VARCHAR(20),
       last_name_two VARCHAR(20),
       email VARCHAR(30)
     )""";
  public static final String QUOTE_TABLE_CREATE = """
     CREATE TABLE IF NOT EXISTS quotes(
       id INTEGER AUTO_INCREMENT PRIMARY KEY,
       client_id VARCHAR(80) NOT NULL,
       equipment_id VARCHAR(40),
       amount INTEGER,
       price DOUBLE,
       created_at DATE,
       FOREIGN KEY (client_id) REFERENCES clients(id),
       FOREIGN KEY (equipment_id) REFERENCES equipments(id)
     )""";
  public static final String EQUIPMENT_TABLE_CREATE = """
     CREATE TABLE IF NOT EXISTS equipments(
       id VARCHAR(40) PRIMARY KEY NOT NULL,
       description TEXT,
       u_value DOUBLE,
       stock INTEGER,
       manufacturer_id VARCHAR(40)
     )""";
  public static final String CLIENT_INFORMATION_RETRIEVE =
     "SELECT name, last_name_one, last_name_two, email FROM clients WHERE id = ?";
  public static final String CLIENT_INFORMATION_SAVE = """
     INSERT INTO clients(id, name, last_name_one, last_name_two, email)
     VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATED KEY UPDATE
     name = VALUES(name),
     last_name_one = VALUES(last_name_one),
     last_name_two = VALUES(last_name_two),
     email = VALUES(email);
     """;
  public static final String QUOTE_INFORMATION_RETRIEVE =
     "SELECT client_id, equipment_id, amount, price, created_at FROM quotes WHERE id = ?";
  public static final String QUOTES_RETRIEVING_BY_CLIENT_ID =
     "SELECT quotes.id, equipment_id, amount, price, created_at FROM quotes WHERE client_id = ?";
  public static final String QUOTE_INFORMATION_RETRIEVE_BY_BOTH_IDS =
     "SELECT equipment_id, amount, price, created_at FROM quotes WHERE id = ? AND client_id = ?";
  public static final String CLIENT_INFORMATION_DELETE = "DELETE FROM clients WHERE id = ?;";
  public static final String QUOTE_INFORMATION_SAVE = """
     INSERT INTO quotes(id, client_id, equipment_id, amount, price, created_at)
     VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATED KEY UPDATE
     client_id = VALUES(client_id),
     equipment_id = VALUES(equipment_id),
     amount = VALUES(amount),
     price = VALUES(price),
     created_at = VALUES(created_at);
     """;
  public static final String QUOTE_INFORMATION_DELETE = "DELETE FROM quotes WHERE id = ?;";
  public static final String EQUIPMENT_INFORMATION_RETRIEVE =
     "SELECT description, u_value, stock, manufacturer_id FROM equipments WHERE id = ?;";
  public static final String EQUIPMENT_INFORMATION_SAVE = """
     INSERT INTO equipments(id, description, u_value, stock, manufacturer_id) VALUES (?, ?, ?, ?, ?)
     ON DUPLICATED KEY UPDATE
     description = VALUES(description),
     u_value = VALUES(u_value),
     stock = VALUES(stock),
     manufacturer_id = VALUES(manufacturer_id);""";
  public static final String EQUIPMENT_INFORMATION_DELETE = "DELETE FROM equipments WHERE id = ?;";
}
