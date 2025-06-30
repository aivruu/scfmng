package io.github.aivruu.scfmng.controller.client;

import io.github.aivruu.scfmng.controller.QuotesController;
import io.github.aivruu.scfmng.model.Client;
import io.github.aivruu.scfmng.model.Quote;

import java.util.List;

/**
 * Controller used for general client-quotes related-operations.
 * <p>
 * This controller's main-purpose is to provide methods to manage quotes, interacting with both as
 * client and quotes controllers, and provide a specific-manager for the quotes-management for a
 * specific client.
 *
 * @since 1.0.0
 */
public final class ClientQuotesController {
  private final ClientController clientController;
  private final QuotesController quotesController;

  /**
   * Creates a new {@link ClientQuotesController} with the provided parameters.
   *
   * @param clientController the {@link ClientController}.
   * @param quotesController the {@link QuotesController}.
   * @since 1.0.0
   */
  public ClientQuotesController(
     final ClientController clientController,
     final QuotesController quotesController) {
    this.clientController = clientController;
    this.quotesController = quotesController;
  }

  /**
   * Searches the requested-quote with the provided id for the specified client.
   * <p>
   * The method will search through {@link Client#quotes()} by requested-quote, if found, it will
   * return it. Otherwise, the method will fetch the quote from the database, if the quote exists
   * it will add it to the {@link Client#quotes()} and client's information will be saved.
   *
   * @param clientId the client's id.
   * @param id the quote's id.
   * @return The requested {@link Quote} or {@code null} if the client does not exist, or quote does
   * not exist in the database's registry.
   * @see ClientController#findClientData(String)
   * @see Client#quoteById(String)
   * @see QuotesController#findClientQuote(String, String)
   * @since 1.0.0
   */
  public Quote findQuoteForClientById(final String clientId, final String id) {
    final Client client = this.clientController.findClientData(clientId).join();
    if (client == null) {
      return null;
    }
    Quote quote = client.quoteById(id);
    if (quote != null) {
      return quote;
    }
    /*
     * We need to fetch the quote from the database, if the quote exists in the table, we've to add
     * it to the client's quotes-list, and persist that information.
     */
    quote = this.quotesController.findClientQuote(clientId, id).join();
    if (quote != null) {
      // ignore result as at this point quote doesn't exist for client's quotes.
      client.addQuote(quote);
      // result can be ignored as if it fails, the quote may be saved later by the user.
      this.clientController.saveClientData(client).join();
    }
    return quote;
  }

  /**
   * Retrieves all the quotes that belongs to the client with the provided id.
   * <p>
   * If there are quotes belonging to the client retrieved from the database, they will be added to
   * the {@link Client#quotes()}, and the client's information will be saved.
   *
   * @param clientId the client's id.
   * @return A {@link List} of {@link Quote}, or empty if the client does not exist or there are no
   * quotes created by the client.
   * @see ClientController#findClientData(String)
   * @see QuotesController#findClientQuotes(String)
   * @since 1.0.0
   */
  public List<Quote> findQuotesForClient(final String clientId) {
    final Client client = this.clientController.findClientData(clientId).join();
    if (client == null) {
      return List.of();
    }
    List<Quote> quotes = client.quotes();
    if (!quotes.isEmpty()) {
      return quotes;
    }
    // if there are quotes for this client in the database, we need to add them to the client's quotes.
    quotes = this.quotesController.findClientQuotes(clientId).join();
    if (!quotes.isEmpty()) {
      for (final Quote quote : quotes) {
        // ignore result as at this point quotes don't exist for client's quotes.
        client.addQuote(quote);
      }
      // ignored result here for the same reason as above.
      this.clientController.saveClientData(client).join();
    }
    return quotes; // may be empty or not.
  }

  /**
   * Adds the given {@link Quote} to the {@link Client#quotes()} and the quotes-table in the
   * database, and saves the information for both.
   *
   * @param clientId the client's id.
   * @param quote the {@link Quote} to be added.
   * @return {@code true} if the quote and client-data were saved correctly, {@code false} otherwise.
   * @see ClientController#findClientData(String)
   * @see QuotesController#saveQuote(Quote)
   * @see ClientController#saveClientData(Client)
   * @since 1.0.0
   */
  public boolean addQuote(final String clientId, final Quote quote) {
    final Client client = this.clientController.findClientData(clientId).join();
    if (client == null) {
      return false;
    }
    client.addQuote(quote); // ignore as quote could have been added prior to this call.
    return this.quotesController.saveQuote(quote)
       .exceptionally(exception -> {
         exception.printStackTrace();
         return false;
       }).join()
       && this.clientController.saveClientData(client)
       .exceptionally(exception -> {
         exception.printStackTrace();
         return false;
       })
       .join();
  }

  /**
   * Deletes the given {@link Quote} from the {@link Client#quotes()} and the quotes-table in the
   * database.
   *
   * @param client the {@link Client}.
   * @param quote the {@link Quote} to be deleted.
   * @return {@code true} if the quote and client-data were saved correctly, {@code false} otherwise.
   * @see QuotesController#deleteQuote(String)
   * @see ClientController#saveClientData(Client)
   * @since 1.0.0
   */
  public boolean deleteQuote(final Client client, final Quote quote) {
    // If client's quotes does n
    return client.removeQuote(quote) && this.quotesController.deleteQuote(quote.id()).join()
       && this.clientController.saveClientData(client).join();
  }

  /**
   * Deletes the quote with the provided id for the specified client.
   * <p>
   * This method will search for the quote and client's data in the database, and if found both,
   * will call and return the result of {@link #deleteQuote(Client, Quote)}.
   *
   * @param clientId the client's id.
   * @param id the quote's id.
   * @return {@code true} if the quote was deleted successfully, {@code false} otherwise.
   * @see #deleteQuote(Client, Quote)
   * @since 1.0.0
   */
  public boolean deleteQuote(final String clientId, final String id) {
    final Client client = this.clientController.findClientData(clientId).join();
    if (client == null) {
      return false;
    }
    final Quote quote = client.quoteById(id);
    return (quote != null) && this.deleteQuote(client, quote);
  }
}
