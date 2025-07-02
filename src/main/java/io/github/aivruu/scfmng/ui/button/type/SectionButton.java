package io.github.aivruu.scfmng.ui.button.type;

import io.github.aivruu.scfmng.ui.button.WindowButtonContract;

import javax.swing.*;
import java.awt.*;

public abstract class SectionButton implements WindowButtonContract {
  protected final JButton button;
  protected final JPanel panel;
  // used to add information to the registry area at the main-window if required.
  protected final JTextArea registryArea;

  protected SectionButton(
     final JButton button,
     final JPanel panel,
     final JTextArea registryArea) {
    this.button = button;
    this.panel = panel;
    this.registryArea = registryArea;
  }

  public final JButton button() {
    return this.button;
  }

  public final JPanel panel() {
    return this.panel;
  }

  @Override
  public void build() {
    // common-logic for any section-button.
    this.button.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.panel.add(this.button);
    this.panel.add(Box.createVerticalStrut(10));
  }
}
