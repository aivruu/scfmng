package io.github.aivruu.scfmng.ui;

import io.github.aivruu.scfmng.controller.DatabaseController;
import io.github.aivruu.scfmng.controller.EquipmentController;
import io.github.aivruu.scfmng.controller.client.ClientController;
import io.github.aivruu.scfmng.controller.client.ClientQuotesController;
import io.github.aivruu.scfmng.model.Equipment;
import io.github.aivruu.scfmng.ui.button.helper.RegistryAreaIndexSearcher;
import io.github.aivruu.scfmng.ui.button.type.SectionButton;
import io.github.aivruu.scfmng.ui.button.type.impl.ClientCreationButton;
import io.github.aivruu.scfmng.ui.button.type.impl.ClientDeletionButton;
import io.github.aivruu.scfmng.ui.button.type.impl.QuoteFetchButton;
import io.github.aivruu.scfmng.ui.button.type.impl.StockAvailabilityButton;
import io.github.aivruu.scfmng.ui.button.type.impl.EquipmentCreationButton;
import io.github.aivruu.scfmng.ui.button.type.impl.EquipmentDeletionButton;
import io.github.aivruu.scfmng.ui.button.type.impl.EquipmentModifyButton;
import io.github.aivruu.scfmng.ui.button.type.impl.QuoteCreationButton;
import io.github.aivruu.scfmng.ui.button.type.impl.QuoteDeletionButton;
import io.github.aivruu.scfmng.ui.button.type.impl.QuoteModifyButton;
import io.github.aivruu.scfmng.ui.listener.InventoryWindowCloseListener;

import javax.swing.*;
import java.awt.*;

public final class InventoryUIVisualizer extends JFrame {
  // HOW MANY FIIIEELDS
  private final JPanel mainPanel = new JPanel(new BorderLayout());
  private final JPanel center = new JPanel(new BorderLayout());
  private final JPanel side = new JPanel();
  private final JButton viewerSwitcherButton = new JButton("Switch Viewer");
  private final JTextArea registryArea = new JTextArea("");
  private final DatabaseController databaseController;
  private final EquipmentController equipmentController;
  private final SectionButton[] buttons;

  public InventoryUIVisualizer(
     final DatabaseController databaseController,
     final EquipmentController equipmentController,
     final ClientQuotesController clientQuotesController,
     final ClientController clientController) {
    this.databaseController = databaseController;
    this.equipmentController = equipmentController;
    // who cares about dependency-injection
    this.buttons = new SectionButton[]{
       new StockAvailabilityButton(this.side, this.registryArea, equipmentController),
       new ClientCreationButton(this.side, this.registryArea, clientController),
       new ClientDeletionButton(this.side, this.registryArea, clientController),
       new QuoteCreationButton(this.side, this.registryArea, clientQuotesController,
          clientController,
          equipmentController),
       new QuoteFetchButton(this.side, this.registryArea, clientQuotesController),
       new QuoteModifyButton(this.side, this.registryArea),
       new QuoteDeletionButton(this.side, this.registryArea, clientQuotesController),
       new EquipmentCreationButton(this.side, this.registryArea, equipmentController),
       new EquipmentModifyButton(this.side, this.registryArea),
       new EquipmentDeletionButton(this.side, this.registryArea, equipmentController,
          new RegistryAreaIndexSearcher(this.registryArea))
    };
  }

  public void load() {
    super.setTitle("SCF-MNG - Inventory Management - v0.0.1");
    super.setDefaultCloseOperation(EXIT_ON_CLOSE);
    super.addWindowListener(new InventoryWindowCloseListener(this.databaseController));
    super.setSize(800, 450);
    super.setLocationRelativeTo(null);

    this.registryArea.setEditable(false);
    this.registryArea.setText("Equipment Viewer\n");
    this.fillAreaWithExistingInformation();

    this.center.add(new JScrollPane(this.registryArea), BorderLayout.CENTER);
    this.center.add(this.viewerSwitcherButton, BorderLayout.PAGE_END);
    this.side.setLayout(new BoxLayout(this.side, BoxLayout.Y_AXIS));
    this.side.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    this.mainPanel.add(this.center, BorderLayout.CENTER);
    this.mainPanel.add(this.side, BorderLayout.EAST);

    for (final SectionButton button : this.buttons) {
      button.build();
    }

    super.setContentPane(this.mainPanel);
    super.setVisible(true);
  }

  private void fillAreaWithExistingInformation() {
    this.equipmentController.getAllExisting().thenAccept(equipments -> {
      if (equipments == null || equipments.isEmpty()) {
        JOptionPane.showMessageDialog(this.mainPanel,
           "There's no equipments-information to show in the registry.",
           "Registry Viewer", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      for (final Equipment equipment : equipments) {
        this.registryArea.append(EquipmentCreationButton.CREATED_EQUIPMENT_FORMAT.formatted(
           equipment.id(), equipment.description(), equipment.manufacturer(),
           equipment.unitaryCost(),
           equipment.stock()));
      }
    });
  }
}
