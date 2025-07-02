package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.controller.client.ClientController;
import io.github.aivruu.scfmng.controller.client.ClientQuotesController;
import io.github.aivruu.scfmng.model.Quote;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDate;

public final class QuoteCreationButton extends SectionButton {
  private final ClientQuotesController clientQuotesController;
  private final ClientController clientController;
  private final EquipmentController equipmentController;

  public QuoteCreationButton(
     final JPanel panel,
     final JTextArea registryArea,
     final ClientQuotesController clientQuotesController,
     final ClientController clientController,
     final EquipmentController equipmentController) {
    super(new JButton("Add New Quote"), panel, registryArea);
    this.clientQuotesController = clientQuotesController;
    this.clientController = clientController;
    this.equipmentController = equipmentController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String id = JOptionPane.showInputDialog("Introduce id to identify this quote in the future.");
      if (id == null || id.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No quote-id has been provided.",
           "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (this.clientQuotesController.existsQuote(id)) {
        JOptionPane.showMessageDialog(super.panel, "A quote with that ID already exists.",
           "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Quote quote = this.handleInputAndReturn(id);
      if (quote == null) {
        return;
      }
      if (!this.clientQuotesController.addQuote(quote.clientId(), quote)) {
        JOptionPane.showMessageDialog(super.panel, "An error occurred while saving the information.",
           "Quote Creation", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "The quote %s has been created for the client %s.".formatted(id, quote.clientId()),
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
    });
  }

  private Quote handleInputAndReturn(final String id) {
    final String clientId = JOptionPane.showInputDialog("Introduce client's id that corresponds for this quote.");
    if (clientId == null || clientId.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No client-id has been provided.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    if (this.clientController.findClientData(clientId).join() == null) {
      JOptionPane.showMessageDialog(super.panel, "No client with that ID exists.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String equipmentId = JOptionPane.showInputDialog("Introduce the equipment's id for this quote.");
    if (equipmentId == null || equipmentId.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No equipment-id has been provided.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    if (this.equipmentController.findEquipment(equipmentId).join() == null) {
      JOptionPane.showMessageDialog(super.panel, "No equipment with that ID exists.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String amountInput = JOptionPane.showInputDialog("Introduce the amount of equipment for this quote.");
    if (amountInput == null || amountInput.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No amount has been provided.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final int amount;
    try {
      amount = Integer.parseInt(amountInput);
    } catch (final NumberFormatException exception) {
      JOptionPane.showMessageDialog(super.panel, "The amount provided is not a valid number.",
         "Quote Creation", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    final String priceInput = JOptionPane.showInputDialog("Introduce the price for this quote.");
    if (priceInput == null || priceInput.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No price has been provided.",
         "Quote Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final double price;
    try {
      price = Double.parseDouble(priceInput);
    } catch (final NumberFormatException exception) {
      JOptionPane.showMessageDialog(super.panel, "The price provided is not a valid number.",
         "Quote Creation", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    // yeah...
    return new Quote(id, clientId, equipmentId, amount, price, Date.valueOf(LocalDate.now()));
  }
}
