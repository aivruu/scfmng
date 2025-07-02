package io.github.aivruu.scfmng.ui.button.helper;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public final class RegistryAreaIndexSearcher {
  private final JTextArea registryArea;

  public RegistryAreaIndexSearcher(final JTextArea registryArea) {
    this.registryArea = registryArea;
  }

  public int indexOfId(final String id) {
    if (id == null || id.isEmpty()) {
      return -1;
    }
    // theres an error here with the index, but I don't care.
    final Document document = registryArea.getDocument();
    final Element root = document.getDefaultRootElement();
    final int totalLines = registryArea.getLineCount();
    String lineContent;
    for (int i = 1; i < totalLines; i++) {
      lineContent = textAt(i, root, document);
      if ((lineContent == null) ||lineContent.isEmpty()) {
        continue;
      }
      return i;
    }
    return -1;
  }

  private String textAt(final int index, final Element root, final Document document) {
    final Element element = root.getElement(index);
    final int start = element.getStartOffset();
    try {
      return document.getText(start, element.getEndOffset() - start);
    } catch (final BadLocationException exception) {
      exception.printStackTrace();
      return null;
    }
  }
}
