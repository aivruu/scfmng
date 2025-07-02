package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.client.ClientQuotesController;
import io.github.aivruu.scfmng.model.Quote;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class QuoteDeletionButton extends SectionButton {
  private final ClientQuotesController clientQuotesController;

  public QuoteDeletionButton(
     final JPanel panel,
     final JTextArea registryArea,
     final ClientQuotesController clientQuotesController) {
    super(new JButton("Delete Quote"), panel, registryArea);
    this.clientQuotesController = clientQuotesController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String id = JOptionPane.showInputDialog("Introduce id of the quote to delete.");
      if (id == null || id.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No quote-id has been provided.",
           "Quote Deletion", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Quote quote = this.clientQuotesController.findQuoteById(id);
      if (quote == null) {
        JOptionPane.showMessageDialog(super.panel, "There's no a quote with that ID.",
           "Quote Deletion", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (!this.clientQuotesController.deleteQuote(quote.clientId(), quote.id())) {
        JOptionPane.showMessageDialog(super.panel, "An error occurred while deleting the quote.",
           "Quote Deletion", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel, "The quote %s has been deleted.".formatted(id),
         "Quote Deletion", JOptionPane.INFORMATION_MESSAGE);
    });
  }
}
