package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a Brighten command.
 */
public class Brighten implements ImageCommand {

  int level;
  String oldImg;
  String newImg;

  /**
   * Build a Bright command.
   *
   * @param lv     integer number to adjust
   * @param oldImg name of old image
   * @param newImg name of adjusted image
   */
  public Brighten(int lv, String oldImg, String newImg) {
    this.level = lv;
    this.oldImg = oldImg;
    this.newImg = newImg;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.adjustBrightness(oldImg, level, newImg);
  }
}
