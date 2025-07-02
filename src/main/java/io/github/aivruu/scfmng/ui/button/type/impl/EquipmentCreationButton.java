package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.model.Equipment;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class EquipmentCreationButton extends SectionButton {
  public static final String CREATED_EQUIPMENT_FORMAT = "Equipment-ID: %s, Description: %s, Manufacturer: %s, Unit Cost: %.2f, Stock: %d\n";
  private final EquipmentController equipmentController;

  public EquipmentCreationButton(
     final JPanel panel,
     final JTextArea registryArea,
     final EquipmentController equipmentController) {
    super(new JButton("Add New Equipment"), panel, registryArea);
    this.equipmentController = equipmentController;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog("Introduce the material's id");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No material-id has been provided",
           "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (this.equipmentController.findEquipment(input).join() != null) {
        JOptionPane.showMessageDialog(super.panel,
           "Equipment with id %s already exists".formatted(input), "Equipment Creation",
           JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      final Equipment newEquipment = this.handleInputsAndReturn(input);
      if (newEquipment == null) {
        return;
      }
      if (!this.equipmentController.saveEquipment(newEquipment).join()) {
        JOptionPane.showMessageDialog(super.panel, "An error occurred while saving the equipment",
           "Equipment Creation", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "Created new-equipment with id %s, available units: %d".formatted(input, newEquipment.stock()),
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      if (super.registryArea.getLineCount() == 0) {
        super.registryArea.append("\n");
      }
      super.registryArea.append(CREATED_EQUIPMENT_FORMAT.formatted(input, newEquipment.description(),
         newEquipment.manufacturer(), newEquipment.unitaryCost(), newEquipment.stock()));
    });
  }

  private Equipment handleInputsAndReturn(final String id) {
    final String description = JOptionPane.showInputDialog("Describe the material's purpose.");
    if (description == null || description.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No description has been provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String manufacturer = JOptionPane.showInputDialog("Specify the material's manufacturer");
    if (manufacturer == null || manufacturer.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No manufacturer has been provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String costInput = JOptionPane.showInputDialog("Introduce the material's unitary cost");
    if (costInput == null || costInput.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No unitary-cost has been provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final float unitaryCost;
    try {
      unitaryCost = Float.parseFloat(costInput);
    } catch (final NumberFormatException exception) {
      JOptionPane.showMessageDialog(super.panel, "Invalid unitary-cost provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final String stockInput = JOptionPane.showInputDialog("Introduce the material's stock");
    if (stockInput == null || stockInput.isEmpty()) {
      JOptionPane.showMessageDialog(super.panel, "No stock has been provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    final int stock;
    try {
      stock = Integer.parseInt(stockInput);
    } catch (final NumberFormatException exception) {
      JOptionPane.showMessageDialog(super.panel, "Invalid stock provided",
         "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
      return null;
    }
    return new Equipment(id, description, manufacturer, unitaryCost, stock);
  }
}
