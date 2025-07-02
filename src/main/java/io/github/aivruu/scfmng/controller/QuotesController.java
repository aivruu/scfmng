package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.database.StatementConstants;
import io.github.aivruu.scfmng.model.Quote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@link Controller} implementation for exclusive {@link Quote}s management.
 *
 * @since 0.0.1.
 */
public final class QuotesController extends Controller {
  public QuotesController(final DatabaseController databaseController) {
    super(databaseController);
  }

  @Override
  public boolean start() {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = super.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_TABLE_CREATE)) {
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    }).join();
  }

  /**
   * Searches the information for the quote with the provided id, and returns it if found.
   *
   * @param quoteId the id of the quote.
   * @return A {@link CompletableFuture} that will complete with the {@link Quote} if found,
   * or {@code null} if quote does not exist.
   * @see DatabaseController#connection()
   * @since 0.0.1
   */
  public CompletableFuture<Quote> findQuote(final String quoteId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_RETRIEVE)) {
        statement.setString(1, quoteId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          return !resultSet.next() ? null : new Quote(quoteId, resultSet.getString(1),
             resultSet.getString(2), resultSet.getInt(3), resultSet.getDouble(4), resultSet.getDate(5));
        }
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return null;
      }
    });
  }

  /**
   * Searches the information for the requested-quote for the specified-client, and returns it if
   * found.
   *
   * @param clientId the client's id.
   * @param quoteId the quote's id.
   * @return A {@link CompletableFuture} that will complete with the {@link Quote} if found, or
   * {@code null} if quote does not exist.
   * @see DatabaseController#connection()
   * @since 0.0.1
   */
  public CompletableFuture<Quote> findClientQuote(final String clientId, final String quoteId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_RETRIEVE_BY_BOTH_IDS)) {
        statement.setString(1, quoteId);
        statement.setString(2, clientId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          return !resultSet.next() ? null : new Quote(quoteId, clientId, resultSet.getString(1),
             resultSet.getInt(2), resultSet.getFloat(3), resultSet.getDate(4));
        }
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return null;
      }
    });
  }

  /**
   * Searches and returns all the quotes that belongs to the specified-client.
   *
   * @param clientId the client's id.
   * @return A {@link CompletableFuture} that will complete with a {@link List} of {@link Quote}s,
   * or empty if no quotes were found.
   * @since 0.0.1
   */
  public CompletableFuture<List<Quote>> findClientQuotes(final String clientId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTES_RETRIEVING_BY_CLIENT_ID)) {
        statement.setString(1, clientId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          final List<Quote> quotes = new ArrayList<>(resultSet.getFetchSize());
          while (resultSet.next()) {
            quotes.add(new Quote(resultSet.getString(1), clientId, resultSet.getString(2),
               resultSet.getInt(3), resultSet.getFloat(4), resultSet.getDate(5)));
          }
          return quotes;
        }
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return List.of();
      }
    });
  }

  /**
   * Saves the provided {@link Quote} to the database.
   *
   * @param quote the {@link Quote} to save.
   * @return A {@link CompletableFuture} that will complete with {@code true} if the quote was saved
   * successfully, or {@code false} if an error occurred.
   * @since 0.0.1
   */
  public CompletableFuture<Boolean> saveQuote(final Quote quote) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_SAVE)) {
        statement.setString(1, quote.id());
        statement.setString(2, quote.clientId());
        statement.setString(3, quote.equipmentId());
        statement.setInt(4, quote.amount());
        statement.setDouble(5, quote.price());
        statement.setDate(6, quote.creationDate());
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return false;
      }
    });
  }

  /**
   * Deletes the quote with the specified id from the database.
   *
   * @param quoteId the id of the quote to delete.
   * @return A {@link CompletableFuture} that will complete with {@code true} if the quote was deleted
   * successfully, or {@code null} if an error occurred.
   * @since 0.0.1
   */
  public CompletableFuture<Boolean> deleteQuote(final String quoteId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_DELETE)) {
        statement.setString(1, quoteId);
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        exception.printStackTrace();
        return null;
      }
    });
  }
}
