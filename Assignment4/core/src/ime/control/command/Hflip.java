package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a HorizontalFlip command.
 */
public class Hflip implements ImageCommand {

  String from;
  String to;

  /**
   * Build a horizontal flip command.
   *
   * @param from name of source image
   * @param to name of image producing
   */
  public Hflip(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.horizontalFlip(from, to);
  }
}
