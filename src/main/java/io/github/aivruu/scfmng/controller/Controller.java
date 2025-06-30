package io.github.aivruu.scfmng.controller;

/**
 * Represents a controller in the application that requires to interact with the database.
 *
 * @since 1.0.0
 */
public abstract class Controller {
  protected final DatabaseController databaseController;

  /**
   * Creates a new {@link Controller} with the provided parameters.
   *
   * @param databaseController the {@link DatabaseController}.
   * @since 1.0.0
   */
  protected Controller(final DatabaseController databaseController) {
    this.databaseController = databaseController;
  }

  /**
   * Starts the controller, initializing any necessary resources for its operation.
   *
   * @return {@code true} if the controller started successfully, {@code false} otherwise.
   * @since 1.0.0
   */
  public abstract boolean start();
}
