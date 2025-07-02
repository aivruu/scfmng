package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.client.ClientQuotesController;
import io.github.aivruu.scfmng.model.Quote;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class QuoteFetchButton extends SectionButton {
  private static final String QUOTE_INFORMATION_MESSAGE = """
     Quote-ID: %s
     Quote's Client: %s
     Quote Creation Date: %s
     Quote's Equipment-ID: %s
     Quote's Amount: %d
     Quote's Price: %.2f""";
  private final ClientQuotesController clientQuotesController;

  public QuoteFetchButton(
     final JPanel panel,
     final JTextArea registryArea,
     final ClientQuotesController clientQuotesController) {
    super(new JButton("Fetch Quote"), panel, registryArea);
    this.clientQuotesController = clientQuotesController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog("Introduce the quote's id to check.");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No quote-id has been provided.",
           "Quote Information", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Quote quote = this.clientQuotesController.findQuoteById(input);
      if (quote == null) {
        JOptionPane.showMessageDialog(super.panel, "The requested-quote does not exist.",
           "Quote Information", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel, QUOTE_INFORMATION_MESSAGE.formatted(quote.id(),
         quote.clientId(),
         quote.creationDate().toString(),
         quote.equipmentId(),
         quote.amount(),
         quote.price()));
    });
  }
}
