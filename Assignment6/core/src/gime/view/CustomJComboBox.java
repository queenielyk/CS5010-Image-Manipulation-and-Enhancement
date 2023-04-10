package gime.view;

import javax.swing.JComboBox;

/**
 * A class to implement a customized JComboBox.
 * The EventListener of this JComboBox will not be triggered unless by a click.
 */
public class CustomJComboBox extends JComboBox<String> {

  @Override
  protected void fireActionEvent() {
    if (this.hasFocus()) {
      super.fireActionEvent();
    }
  }
}
