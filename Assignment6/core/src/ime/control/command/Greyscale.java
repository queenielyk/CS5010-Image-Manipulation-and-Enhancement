package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a Greyscale command.
 */
public class Greyscale implements ImageCommand {

  private final String mode;
  private final String from;
  private final String to;

  /**
   * Build a greyscale command.
   *
   * @param mode name of the greyscale mode to apply
   * @param from name of source image
   * @param to   name of image producing
   */
  public Greyscale(String mode, String from, String to) {
    this.mode = mode;
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.colorTrans(mode, from, to);
  }
}
