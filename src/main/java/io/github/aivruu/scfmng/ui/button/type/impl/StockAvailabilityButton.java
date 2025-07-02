package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.model.Equipment;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class StockAvailabilityButton extends SectionButton {
  private final EquipmentController equipmentController;

  public StockAvailabilityButton(
     final JPanel panel,
     final JTextArea registryArea,
     final EquipmentController equipmentController) {
    super(new JButton("Check Product Stock"), panel, registryArea);
    this.equipmentController = equipmentController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog(
         "Specify the material to check for stock-availability");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel,
           "No material has been provided, specify a valid material-id.", "Stock Availability",
           JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Equipment equipment = this.equipmentController.findEquipment(input).join();
      if ((equipment == null) || (equipment.stock() <= 0)) {
        JOptionPane.showMessageDialog(super.panel, "There's no stock-available for that equipment.",
           "Stock Availability", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "There's a remaining-stock of %d units for that equipment.".formatted(equipment.stock()),
         "Stock Availability", JOptionPane.INFORMATION_MESSAGE);
    });
  }
}
