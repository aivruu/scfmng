package io.github.aivruu.scfmng.ui.listener;

import io.github.aivruu.scfmng.controller.DatabaseController;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class InventoryWindowCloseListener implements WindowListener {
  private final DatabaseController databaseController;

  public InventoryWindowCloseListener(final DatabaseController databaseController) {
    this.databaseController = databaseController;
  }

  @Override
  public void windowOpened(final WindowEvent e) {}

  @Override
  public void windowClosing(final WindowEvent e) {
    this.databaseController.shutdown();
  }

  @Override
  public void windowClosed(final WindowEvent e) {}

  @Override
  public void windowIconified(final WindowEvent e) {}

  @Override
  public void windowDeiconified(final WindowEvent e) {}

  @Override
  public void windowActivated(final WindowEvent e) {}

  @Override
  public void windowDeactivated(final WindowEvent e) {}
}
