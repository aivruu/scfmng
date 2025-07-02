package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.client.ClientController;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class ClientDeletionButton extends SectionButton {
  private final ClientController clientController;

  public ClientDeletionButton(
     final JPanel panel,
     final JTextArea registryArea,
     final ClientController clientController) {
    super(new JButton("Delete Client Data"), panel, registryArea);
    this.clientController = clientController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog("Introduce the client's id.");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No client-id has been provided",
           "Client Data Deletion", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (this.clientController.findClientData(input).join() == null) {
        JOptionPane.showMessageDialog(super.panel, "A client with that ID does not exist.",
           "Client Data Deletion", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (!this.clientController.deleteClientData(input).join()) {
        JOptionPane.showMessageDialog(super.panel, "An error occurred while deleting the client data.",
           "Client Data Deletion", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "Client with id %s has been deleted successfully.".formatted(input), "Client Data Deletion",
         JOptionPane.INFORMATION_MESSAGE);
    });
  }
}
