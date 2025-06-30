package io.github.aivruu.scfmng.controller;

import io.github.aivruu.scfmng.controller.abstraction.StatementConstants;
import io.github.aivruu.scfmng.model.Quote;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

  public CompletableFuture<Quote> findQuote(final String quoteId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_RETRIEVE)) {
        statement.setString(1, quoteId);
        try (final ResultSet resultSet = statement.executeQuery()) {
          return !resultSet.next() ? null : new Quote(quoteId, resultSet.getString(2),
             resultSet.getString(4), resultSet.getInt(5), resultSet.getFloat(6), resultSet.getDate(7));
        }
      } catch (final SQLException exception) {
        return null;
      }
    });
  }

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
        return null;
      }
    });
  }

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
        return List.of();
      }
    });
  }

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
        return false;
      }
    });
  }

  public CompletableFuture<Boolean> deleteQuote(final String quoteId) {
    return CompletableFuture.supplyAsync(() -> {
      try (final PreparedStatement statement = this.databaseController.connection().prepareStatement(
         StatementConstants.QUOTE_INFORMATION_DELETE)) {
        statement.setString(1, quoteId);
        statement.executeUpdate();
        return true;
      } catch (final SQLException exception) {
        return null;
      }
    }).exceptionally(exception -> {
      // well..
      exception.printStackTrace();
      return null;
    });
  }
}
