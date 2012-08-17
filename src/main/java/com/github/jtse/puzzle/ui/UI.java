/**
 * 
 */
package com.github.jtse.puzzle.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @author jtse
 */
public class UI {
  /**
   * Shows the open file prompt. This is a blocking operation.
   * 
   * @return File chosen or null if cancelled
   */
  public static final File filePrompt(final String baseDir) {
    final Map<String, File> map = new HashMap<String, File>();

    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          JFrame frame = new JFrame();
          JFileChooser chooser = new JFileChooser();
          chooser.setCurrentDirectory(new File(baseDir));
          int status = chooser.showOpenDialog(frame);

          if (JFileChooser.APPROVE_OPTION == status) {
            map.put("fileName", chooser.getSelectedFile());
          }

          frame.dispose();
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return map.get("fileName");
  }

  public static final void confirm(final String message) {

    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          JFrame frame = new JFrame();

          JOptionPane.showMessageDialog(frame, message);

          frame.dispose();
        }
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
