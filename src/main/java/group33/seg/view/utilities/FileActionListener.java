package group33.seg.view.utilities;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class FileActionListener implements ActionListener {
  private Map<Object, JTextField> mappings = new HashMap<>();
  private JFileChooser fileChooser = new JFileChooser();
  private Component parent;

  public FileActionListener(Component parent, int selectionMode) {
    this.parent = parent;
    fileChooser.setFileSelectionMode(selectionMode);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JTextField txt = mappings.get(e.getSource());
    if (txt == null) {
      System.err.print("Object does not have a corresponding field to update");
      return;
    }

    int res = fileChooser.showOpenDialog(parent);
    if (res == JFileChooser.APPROVE_OPTION) {
      txt.setText(fileChooser.getSelectedFile().getAbsolutePath());
    }
  }

  public void addMapping(Object object, JTextField txt) {
    mappings.put(object, txt);
  }

  public JFileChooser getFileChooser() {
    return fileChooser;
  }
}
