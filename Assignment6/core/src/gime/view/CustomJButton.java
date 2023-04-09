package gime.view;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class CustomJButton extends JButton {
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
