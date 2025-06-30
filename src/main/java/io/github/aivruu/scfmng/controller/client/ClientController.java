package io.github.aivruu.scfmng.controller.client;

import io.github.aivruu.scfmng.controller.Controller;
import io.github.aivruu.scfmng.controller.DatabaseController;
import io.github.aivruu.scfmng.controller.abstraction.StatementConstants;
import io.github.aivruu.scfmng.model.Client;
import io.github.aivruu.scfmng.model.Quote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@link Controller} implementation for exclusive {@link Client} management.
 *
 * @since 1.0.0
 */
public final class ClientController extends Controller {
  public ClientController(final DatabaseController databaseController) {
    super(databaseController);
  }

  @Override
  public boolean start() {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = super.databaseController.connection().prepareStatement(
         StatementConstants.CLIENT_TABLE_CREATE)) {
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    }).join();
  }

  /**
   * Searches the information for the client using the provided id through the database
   * asynchronously.
   * <p>
   *
   * @param id the client's id.
   * @return A {@link CompletableFuture} that will be completed with a {@link Client} or
   * {@code null} if the id does not exist in the database-table.
   * @see DatabaseController#connection()
   * @since 1.0.0
   */
  public CompletableFuture<Client> findClientData(final String id) {
    // Without this, it'll be very slow, and we can't even load it in cache.
    return CompletableFuture.supplyAsync(() -> {
      try (
         final PreparedStatement clientStatement = super.databaseController.connection().prepareStatement(
            StatementConstants.CLIENT_INFORMATION_RETRIEVE);
         final PreparedStatement quotesStatement = super.databaseController.connection().prepareStatement(
            StatementConstants.QUOTES_RETRIEVING_BY_CLIENT_ID)) {
        clientStatement.setString(1, id);
        try (
           final ResultSet clientResultSet = clientStatement.executeQuery();
           final ResultSet quotesResultSet = quotesStatement.executeQuery()) {
          if (!clientResultSet.next()) {
            return null;
          }
          final List<Quote> quotes  = new ArrayList<>();
          while (quotesResultSet.next()) {
            quotes.add(new Quote(quotesResultSet.getString(1), id, quotesResultSet.getString(2),
               quotesResultSet.getInt(3), quotesResultSet.getFloat(4), quotesResultSet.getDate(5)));
          }
          return new Client(id, clientResultSet.getString(1), clientResultSet.getString(2),
             clientResultSet.getString(3), quotes, clientResultSet.getString(4));
        }
      } catch (final SQLException exception) {
        // oops.
        return null;
      }
    });
  }

  /**
   * Saves the client's information into the database asynchronously.
   *
   * @param client the {@link Client} to save.
   * @return A {@link CompletableFuture} that will be completed with {@code true} if the client's
   * data was saved, {@code false} otherwise.
   * @see DatabaseController#connection()
   * @since 1.0.0
   */
  public CompletableFuture<Boolean> saveClientData(final Client client) {
    return CompletableFuture.supplyAsync(() -> {
      try (final Connection connection = super.databaseController.connection();
           final PreparedStatement clientStatement = connection.prepareStatement(
              StatementConstants.CLIENT_INFORMATION_SAVE)) {
        clientStatement.setString(1, client.id());
        clientStatement.setString(2, client.name());
        clientStatement.setString(3, client.lastNameOne());
        clientStatement.setString(4, client.lastNameTwo());
        clientStatement.setString(5, client.email());
        clientStatement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        return false;
      }
    });
  }

  /**
   * Deletes the client's data from the database asynchronously.
   *
   * @param id the client's id.
   * @return A {@link CompletableFuture} that will be completed with {@code true} if the client's
   * information was deleted, {@code false} otherwise.
   * @see DatabaseController#connection()
   * @since 1.0.0
   */
  public CompletableFuture<Boolean> deleteClientData(final String id) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = super.databaseController.connection().prepareStatement(
         StatementConstants.CLIENT_INFORMATION_DELETE)) {
        statement.setString(1, id);
        return statement.execute();
      } catch (final SQLException exception) {
        return null;
      }
    });
  }
}
