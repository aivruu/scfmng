package io.github.aivruu.scfmng.model;

import java.sql.Date;

public final class Quote {
  private final String id;
  private final String clientId;
  private final Date creationDate;
  private String equipmentId;
  private int amount;
  private double price;

  public Quote(
     final String id,
     final String clientId,
     final String equipmentId,
     final int amount,
     final double price,
     final Date creationDate) {
    this.id = id;
    this.clientId = clientId;
    this.creationDate = creationDate;
    this.equipmentId = equipmentId;
    this.amount = amount;
    this.price = price;
  }

  public String id() {
    return this.id;
  }

  public String clientId() {
    return this.clientId;
  }

  public String equipmentId() {
    return this.equipmentId;
  }

  public int amount() {
    return this.amount;
  }

  public double price() {
    return this.price;
  }

  public Date creationDate() {
    return this.creationDate;
  }

  public void equipmentId(final String equipmentId) {
    this.equipmentId = equipmentId;
  }

  public void amount(final int amount) {
    this.amount = amount;
  }

  public void price(final float price) {
    this.price = price;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    return (obj instanceof Quote quote) && this.id.equals(quote.id);
  }
}
