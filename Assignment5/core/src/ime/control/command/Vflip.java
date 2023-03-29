package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a VerticalFlip command.
 */
public class Vflip implements ImageCommand {

  private final String from;
  private final String to;

  /**
   * Build a vflip command
   *
   * @param from Source Image Name
   * @param to   Dest Image Name
   */
  public Vflip(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.verticalFlip(from, to);
  }
}
