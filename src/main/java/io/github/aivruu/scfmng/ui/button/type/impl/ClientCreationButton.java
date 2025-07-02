package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.client.ClientController;
import io.github.aivruu.scfmng.model.Client;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;
import java.util.ArrayList;

public final class ClientCreationButton extends SectionButton {
  private final ClientController clientController;

  public ClientCreationButton(
     final JPanel panel,
     final JTextArea registryArea,
     final ClientController clientController) {
    super(new JButton("Create Client Data"), panel, registryArea);
    this.clientController = clientController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog("Introduce the client's id.");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No client-id has been provided",
           "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (this.clientController.findClientData(input).join() != null) {
        JOptionPane.showMessageDialog(super.panel, "A client with that ID already exists.",
           "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Client client = this.handleInputsAndReturn(input);
      if (client == null) {
        return;
      }
      if (!this.clientController.saveClientData(client).join()) {
        JOptionPane.showMessageDialog(super.panel, "An error occurred while deleting the client data.",
           "Client Data Creation", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "A client with ID %s has been created.".formatted(input), "Client Data Creation",
         JOptionPane.INFORMATION_MESSAGE);
    });
  }

  private Client handleInputsAndReturn(final String id) {
    final String name = JOptionPane.showInputDialog("Introduce the client's name.");
    if (name == null || name.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No client-name has been provided",
         "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String firstLastName = JOptionPane.showInputDialog("Introduce the client's first last name.");
    if (firstLastName == null || firstLastName.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No first last-name has been provided",
         "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String secondLastName = JOptionPane.showInputDialog("Introduce the client's second last name.");
    if (secondLastName == null || secondLastName.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No second last-name has been provided",
         "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String email = JOptionPane.showInputDialog("Introduce the client's email address.");
    if (email == null || email.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No client-email has been provided",
         "Client Data Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    return new Client(id, name, firstLastName, secondLastName, new ArrayList<>(), email);
  }
}
