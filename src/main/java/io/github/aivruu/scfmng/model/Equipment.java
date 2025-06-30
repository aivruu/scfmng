package io.github.aivruu.scfmng.model;

public final class Equipment {
  private final String id;
  private final String description;
  private final String manufacturer;
  private float unitaryCost;
  private int stock;

  public Equipment(
     final String id,
     final String description,
     final String manufacturer,
     final float unitaryCost,
     final int stock) {
    this.id = id;
    this.description = description;
    this.manufacturer = manufacturer;
    this.unitaryCost = unitaryCost;
    this.stock = stock;
  }

  public String id() {
    return this.id;
  }

  public String description() {
    return this.description;
  }

  public String manufacturer() {
    return this.manufacturer;
  }

  public float unitaryCost() {
    return this.unitaryCost;
  }

  public int stock() {
    return this.stock;
  }

  public void unitaryCost(final float unitaryCost) {
    this.unitaryCost = unitaryCost;
  }

  public void stock(final int stock) {
    this.stock = stock;
  }
}
