package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a VerticalFlip command.
 */
public class Vflip implements ImageCommand {
  String from;
  String to;

  public Vflip(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void go(ImageProcessor model) {
    model.verticalFlip(from, to);
  }
}
