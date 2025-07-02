package io.github.aivruu.scfmng.ui.button.type.impl;

import io.github.aivruu.scfmng.ui.button.type.SectionButton;

import javax.swing.*;

public final class QuoteModifyButton extends SectionButton {
  public QuoteModifyButton(final JPanel panel, final JTextArea registryArea) {
    super(new JButton("Modify Quote"), panel, registryArea);
  }

  @Override
  public void build() {
    super.build();
  }
}
