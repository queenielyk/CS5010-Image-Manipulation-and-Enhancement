package gime.view;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 * A class to implement a customized JButton.
 * This JButton has no border, focus, and background.
 * It is represented by an icon with a fixed size only.
 */
public class CustomJButton extends JButton {
  /**
   * A constructor to construct a customized JButton with specified icon image.
   *
   * @param icon the filepath of icon image
   * @throws IOException unable to read this icon image
   */
  public CustomJButton(String icon) throws IOException {
    super();
    setOpaque(false);
    setFocusPainted(false);
    setBorderPainted(false);
    setContentAreaFilled(false);
    setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    Image img = ImageIO.read(getClass().getResource(icon));
    setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
  }

}
