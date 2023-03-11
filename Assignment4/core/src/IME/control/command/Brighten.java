package IME.control.command;

import IME.control.ImageCommand;
import IME.model.ImageProcessor;

/**
 * This class represent a Brighten command.
 */
public class Brighten implements ImageCommand {
  int level;
  String oldImg;
  String newImg;

  public Brighten(int lv, String oldImg, String newImg) {
    this.level = lv;
    this.oldImg = oldImg;
    this.newImg = newImg;
  }

  @Override
  public void go(ImageProcessor model) {
    model.adjustBrightness(oldImg, level, newImg);
  }
}
