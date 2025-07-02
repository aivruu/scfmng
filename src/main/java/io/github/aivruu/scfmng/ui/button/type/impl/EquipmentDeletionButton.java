package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;
import io.github.aivruu.scfmng.ui.button.helper.RegistryAreaIndexSearcher;

import javax.swing.*;

public final class EquipmentDeletionButton extends SectionButton {
  private final EquipmentController equipmentController;
  private final RegistryAreaIndexSearcher registryAreaIndexSearcher;

  public EquipmentDeletionButton(
     final JPanel panel,
     final JTextArea registryArea,
     final EquipmentController equipmentController,
     final RegistryAreaIndexSearcher registryAreaIndexSearcher) {
    super(new JButton("Remove Equipment"), panel, registryArea);
    this.equipmentController = equipmentController;
    this.registryAreaIndexSearcher = registryAreaIndexSearcher;
  }

  @Override
  public void build() {
    super.build();
    super.button.addActionListener(_ -> {
      final String input = JOptionPane.showInputDialog("Introduce the material's id to delete.");
      if (input == null || input.isEmpty()) {
        JOptionPane.showMessageDialog(super.panel, "No material-id has been provided",
           "Equipment Creation", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      // ugly as** way, we eat two lookups to the database just here.
      if (this.equipmentController.findEquipment(input).join() == null) {
        JOptionPane.showMessageDialog(super.panel, "That equipment-type does not exist.",
           "Equipment Deletion", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      if (!this.equipmentController.deleteEquipment(input).join()) {
        JOptionPane.showMessageDialog(super.panel,
           "An error occurred while deleting the equipment.\nMight be it does not exist.",
           "Equipment Deletion", JOptionPane.ERROR_MESSAGE);
        return;
      }
      JOptionPane.showMessageDialog(super.panel,
         "Equipment with id %s has been deleted.".formatted(input), "Equipment Deletion",
         JOptionPane.INFORMATION_MESSAGE);
      // ?
      final int lineIndex = this.registryAreaIndexSearcher.indexOfId(input);
      if (lineIndex > 0){
        super.registryArea.remove(lineIndex);
      }
    });
  }
}
