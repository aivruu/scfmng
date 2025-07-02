package io.github.aivruu.scfmng.model;

import java.util.List;

/**
 * Represents a client in the application.
 *
 * @since 0.0.1
 */
public final class Client {
  private final String id;
  private final String name;
  private final String lastNameOne;
  private final String lastNameTwo;
  private final List<Quote> quotes;
  private String email;

  /**
   * Creates a new {@link Client} with the provided parameters.
   *
   * @param id the client's id.
   * @param name the client's name.
   * @param lastNameOne the client's first last name.
   * @param lastNameTwo the client's second last name.
   * @param quotes the client's quotes (retrieved from the database).
   * @param email the client's email address.
   * @since 0.0.1
   */
  public Client(
     final String id,
     final String name,
     final String lastNameOne,
     final String lastNameTwo,
     final List<Quote> quotes,
     final String email) {
    this.id = id;
    this.name = name;
    this.lastNameOne = lastNameOne;
    this.lastNameTwo = lastNameTwo;
    this.quotes = quotes;
    this.email = email;
  }

  /**
   * Returns the client's id.
   *
   * @return The client's id.
   * @since 0.0.1
   */
  public String id() {
    return this.id;
  }

  /**
   * Returns the client's name.
   *
   * @return The client's name.
   * @since 0.0.1
   */
  public String name() {
    return this.name;
  }

  /**
   * Returns the client's first last name.
   *
   * @return The client's first last name.
   * @since 0.0.1
   */
  public String lastNameOne() {
    return this.lastNameOne;
  }

  /**
   * Returns the client's second last name.
   *
   * @return The client's second last name.
   * @since 0.0.1
   */
  public String lastNameTwo() {
    return this.lastNameTwo;
  }

  /**
   * Returns the client's quotes.
   *
   * @return The client's quotes.
   * @since 0.0.1
   */
  public List<Quote> quotes() {
    return this.quotes;
  }

  public Quote quoteById(final String id) {
    for (final Quote quote : this.quotes) {
      if (quote.id().equals(id)) {
        return quote;
      }
    }
    return null;
  }

  public String email() {
    return this.email;
  }

  public void email(final String email) {
    this.email = email;
  }

  public boolean addQuote(final Quote quote) {
    return this.quotes.add(quote);
  }

  public boolean removeQuote(final Quote quote) {
    return this.quotes.remove(quote);
  }

  public boolean removeQuoteById(final String id) {
    for (final Quote quote : this.quotes) {
      if (quote.id().equals(id)) {
        return this.quotes.remove(quote); // should return [true] always at this point.
      }
    }
    return false;
  }
}
