package io.github.aivruu.scfmng;

/**
 * First entry-point for the application, any required check is performed here before call to the
 * {@link Main#main(String[])} function.
 *
 * @since 0.0.1
 */
public final class Boostrap {
  public static void main(final String[] args) {
    // first entry-point, check if required database-driver is included for database-connection,
    // otherwise, stop application start-up.
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (final ClassNotFoundException exception) {
      System.err.println("The JDBC-Driver was not found for the database-connection, please include it before run the application.");
      return;
    }
    Main.main(args);
  }
}
