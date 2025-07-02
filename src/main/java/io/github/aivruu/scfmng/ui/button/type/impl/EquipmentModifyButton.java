package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class EquipmentModifyButton extends SectionButton {
  public EquipmentModifyButton(final JPanel panel, final JTextArea registryArea) {
    super(new JButton("Modify Equipment"), panel, registryArea);
  }
}
