package gime.view;

import javax.swing.JComboBox;

public class CustomJComboBox extends JComboBox<String> {

  @Override
  protected void fireActionEvent() {
    if (this.hasFocus()) {
      super.fireActionEvent();
    }
  }
}
